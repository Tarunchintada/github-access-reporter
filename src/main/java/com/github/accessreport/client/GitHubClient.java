package com.github.accessreport.client;

import com.github.accessreport.model.Collaborator;
import com.github.accessreport.model.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GitHubClient {

    private static final Logger log = LoggerFactory.getLogger(GitHubClient.class);

    private final RestTemplate restTemplate;

    @Value("${github.token}")
    private String token;

    @Value("${github.base-url}")
    private String baseUrl;

    @Value("${github.org}")
    private String org;

    @Value("${github.per-page}")
    private int perPage;

    public GitHubClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // calling GitHub API to get all repositories from the organization
    public List<Repository> getRepositories() {

        List<Repository> allRepos = new ArrayList<>();
        int page = 1;

        while (true) {

            // GitHub returns limited results per page, so using pagination
            String url = baseUrl + "/orgs/" + org +
                    "/repos?per_page=" + perPage + "&page=" + page;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Repository[]> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, Repository[].class);

            checkRateLimit(response.getHeaders());

            Repository[] repos = response.getBody();

            // if no repositories returned, it means we reached the last page
            if (repos == null || repos.length == 0) {
                break;
            }

            allRepos.addAll(Arrays.asList(repos));
            page++;
        }

        log.info("Total repositories fetched: {}", allRepos.size());

        return allRepos;
    }


    public List<Collaborator> getCollaborators(String repo) {

        String url = baseUrl + "/repos/" + org + "/" + repo + "/contributors";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Collaborator[]> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, Collaborator[].class);


        // checking GitHub rate limit to avoid API blocking
        checkRateLimit(response.getHeaders());

        return Arrays.asList(response.getBody());
    }

    //RATE LIMIT CHECK
    private void checkRateLimit(HttpHeaders headers) {

        String remaining = headers.getFirst("X-RateLimit-Remaining");

        if (remaining != null) {

            int remainingCalls = Integer.parseInt(remaining);

            if (remainingCalls < 50) {
                log.warn("GitHub API rate limit low: {} requests remaining", remainingCalls);
            }
        }
    }
}