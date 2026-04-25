package com.example.jobexchange.config;

import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.JobPost;
import com.example.jobexchange.domain.JobStatus;
import com.example.jobexchange.domain.UserRole;
import com.example.jobexchange.repository.AppUserRepository;
import com.example.jobexchange.repository.JobApplicationRepository;
import com.example.jobexchange.repository.JobPostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedData(AppUserRepository users, JobPostRepository jobs, JobApplicationRepository applications, JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.execute("UPDATE app_users SET password = '123456' WHERE password IS NULL OR password = ''");

            if (users.count() > 0 || jobs.count() > 0 || applications.count() > 0) {
                return;
            }

            AppUser employer = users.save(new AppUser(
                    "Nguyen Minh Quan",
                    "hr@viettalent.vn",
                    "0901234567",
                    "123456",
                    UserRole.EMPLOYER,
                    "Cong ty Co phan VietTalent",
                    "Truong phong tuyen dung"
            ));
            users.save(new AppUser(
                    "Tran Thu Ha",
                    "ha.tran@example.com",
                    "0912345678",
                    "123456",
                    UserRole.JOB_SEEKER,
                    null,
                    "Java fresher, da lam do an Spring Boot"
            ));
            users.save(new AppUser(
                    "Le Quoc Bao",
                    "bao.le@example.com",
                    "0987654321",
                    "123456",
                    UserRole.JOB_SEEKER,
                    null,
                    "Frontend React intern"
            ));
            users.save(new AppUser(
                    "Quan tri Trung tam DVVL",
                    "admin@dvvl.example",
                    "0243000000",
                    "123456",
                    UserRole.ADMIN,
                    "Trung tam Dich vu viec lam Ha Noi",
                    "Can bo kiem duyet tin tuyen dung"
            ));

            JobPost javaJob = new JobPost(
                    "Lap trinh vien Java Fresher",
                    "Cong ty Co phan VietTalent",
                    "Ha Noi",
                    "10 - 15 trieu VND",
                    "Tham gia phat trien module quan ly ho so ung vien, tich hop API va bao tri he thong tuyen dung.",
                    "Nam Java core, SQL co ban, biet Spring Boot la loi the.",
                    employer
            );
            javaJob.approve();
            jobs.save(javaJob);

            jobs.save(new JobPost(
                    "Nhan vien tu van viec lam",
                    "Cong ty Co phan VietTalent",
                    "Ha Noi",
                    "8 - 12 trieu VND",
                    "Tiep nhan nhu cau ung vien, tu van vi tri phu hop va ho tro hoan thien ho so ung tuyen.",
                    "Giao tiep tot, su dung thanh thao tin hoc van phong.",
                    employer
            ));
        };
    }
}
