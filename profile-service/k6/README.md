# K6 load test for profile service

This script targets the profile service backed by Neo4j to measure response time while retrieving user profiles.

## Prerequisites
- [k6](https://k6.io/docs/getting-started/installation/) installed locally
- Profile service running (default: `http://localhost:8083/profile`) and connected to Neo4j

## Running the test
```bash
# from repository root
k6 run profile-service/k6/profile-get-user.js \
  --env PROFILE_BASE_URL=http://localhost:8083/profile
```

The test will:
1. Create a profile using the public `/internal/users` endpoint.
2. Generate a lightweight JWT accepted by the service.
3. Hit `GET /users/{profileId}` under a ramping VU load with thresholds on 95th percentile latency and check success rate.
