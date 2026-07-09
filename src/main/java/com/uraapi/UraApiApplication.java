package com.uraapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目入口。
 * @EnableScheduling 是为将来第二步加"每日定时抓取"预留的，现在先不启用具体任务。
 */
@SpringBootApplication
@EnableScheduling
public class UraApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UraApiApplication.class, args);
    }
}
