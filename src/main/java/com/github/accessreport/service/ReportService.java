package com.github.accessreport.service;

import com.github.accessreport.client.GitHubClient;
import com.github.accessreport.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final GitHubClient client;
    private final ExecutorService executor;

    @Value("${github.org}")
    private String org;

    public ReportService(GitHubClient client, ExecutorService executor){
        this.client = client;
        this.executor = executor;
    }


    // caching the report so repeated requests don't hit GitHub API again
    @Cacheable(value="accessReport")
    public AccessReport generateReport() throws Exception {

        long start = System.currentTimeMillis();

        log.info("Generating access report for organization: {}", org);

        // fetching all repositories from GitHub organization
        List<Repository> repos = client.getRepositories();

        log.info("Total repositories found: {}", repos.size());

        // map to store user -> list of repositories they have access to
        ConcurrentHashMap<String,List<String>> map = new ConcurrentHashMap<>();

        List<Future<?>> futures = new ArrayList<>();

        for(Repository repo : repos){

            // processing each repository in parallel to improve performance
            Future<?> future = executor.submit(() -> {

                // fetching contributors for this repository
                List<Collaborator> collaborators =
                        client.getCollaborators(repo.getName());

                for(Collaborator c : collaborators){

                    map.computeIfAbsent(c.getLogin(),
                                    k -> Collections.synchronizedList(new ArrayList<>()))
                            .add(repo.getName());

                }

            });

            futures.add(future);
        }

        // waiting for all parallel tasks to complete
        for(Future<?> f : futures){
            f.get();
        }

        List<UserAccess> users =
                map.entrySet()
                        .stream()
                        .map(e -> new UserAccess(e.getKey(), e.getValue()))
                        .toList();

        long end = System.currentTimeMillis();
        log.info("Report generated in {} ms", (end-start));

        return new AccessReport(
                org,
                repos.size(),
                users.size(),
                users
        );
    }
}