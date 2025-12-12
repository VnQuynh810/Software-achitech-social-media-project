import http from 'k6/http';
import { check, sleep } from 'k6';

// --- CẤU HÌNH ---
// 1. URL gốc của Profile Service
const BASE_URL = 'http://localhost:8083/profile';

// 2. Đường dẫn tới API MySQL (Sửa lại nếu bạn đặt tên khác trong Controller)
// Ví dụ: nếu controller là @GetMapping("/mysql/users/{id}")
const SQL_ENDPOINT = '/mysql/users';

// 3. ID dữ liệu thật (Lấy từ ảnh Neo4j/MySQL bạn vừa import)
const TARGET_USER_ID = "f0b4058a-c6ef-4f2f-895d-72f093c27909";

export const options = {
    // Kịch bản tải (Load Scenario)
    scenarios: {
        benchmark_mysql: {
            executor: 'ramping-vus', // Tăng dần lượng user ảo
            startVUs: 0,
            stages: [
                { duration: '5s', target: 5 },   // Khởi động nhẹ: 5 user
                { duration: '10s', target: 20 }, // Tải cao: 20 user dồn dập
                { duration: '10s', target: 20 }, // Duy trì 20 user trong 10s
                { duration: '5s', target: 0 },   // Giảm dần về 0
            ],
            gracefulRampDown: '5s',
        },
    },
    // Ngưỡng đánh giá (Pass/Fail)
    thresholds: {
        // 95% request phải phản hồi dưới 200ms (SQL thường rất nhanh với ID index)
        http_req_duration: ['p(95)<200'],
        // Tỷ lệ lỗi phải dưới 1%
        http_req_failed: ['rate<0.01'],
    },
};

export default function () {
    // Tạo URL: http://localhost:8083/profile/mysql/users/f0b4058a...
    const url = `${BASE_URL}${SQL_ENDPOINT}/${TARGET_USER_ID}`;

    const params = {
        headers: {
            'Content-Type': 'application/json',
            // Không cần Authorization vì bạn đã mở public ở SecurityConfig
        },
        tags: {
            db_type: 'MySQL' // Gắn thẻ để lọc trên Grafana/Dashboard
        },
    };

    const res = http.get(url, params);

    // --- DEBUG: In lỗi nếu không lấy được ---
    if (res.status !== 200) {
        console.error(`❌ MySQL Fail [${res.status}]: ${res.body}`);
    }

    check(res, {
        'MySQL Status 200': (r) => r.status === 200,
        // Kiểm tra body có chứa đúng ID không (đảm bảo không bị cache sai hoặc trả về rác)
        'MySQL Data Correct': (r) => r.body.includes(TARGET_USER_ID),
    });

    // Nghỉ 1 xíu giữa các request để giả lập người dùng thật (tùy chọn)
    // Nếu muốn test max throughput (TPS) thì comment dòng sleep này lại
    sleep(0.5);
}