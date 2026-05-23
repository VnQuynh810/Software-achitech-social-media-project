import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

const rateLimitedCount = new Counter('bi_chan_429');
const successCount = new Counter('thanh_cong_200');

export const options = {
    scenarios: {
        login_flood: {
            executor: 'ramping-arrival-rate',
            startRate: 2,
            timeUnit: '1s',
            preAllocatedVUs: 20,
            maxVUs: 100,
            stages: [
                { target: 10, duration: '10s' },
                { target: 50, duration: '20s' },
                { target: 5, duration: '10s' },
            ],
        },
    },
};

export default function () {

    const url = 'http://localhost:8888/api/v1/auth/login';


    const payload = JSON.stringify({
        email: "nguyen@gmail.com",
        password: "123456"
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',

        },
    };


    const res = http.post(url, payload, params);


    if (res.status === 429) {
        rateLimitedCount.add(1);
    } else if (res.status === 200) {
        successCount.add(1);
    } else {

        console.log(`Status khác: ${res.status}`);
    }
}