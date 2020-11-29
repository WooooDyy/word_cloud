package com.example.cloud.component;

import com.example.cloud.HistoryInfo;
import com.example.cloud.InfoVO;
import com.example.cloud.utils.HttpClientUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Scheduled(fixedRate = 600000)
    private void configureTasks() {
        String s = HttpClientUtil.doGet("http://49.235.254.126:5050/Top")
                .replace(" ", "").replace("\"", "").replace("\n", "");
        s = s.substring(1, s.length()-1);
        String[] split = s.split(",");

        SimpleDateFormat df = new SimpleDateFormat("dd HH:mm:ss");//设置日期格式
        // new Date()为获取当前系统时间
        System.out.println(df.format(new Date()));

        List<InfoVO> cur = new ArrayList<>();
        for (String sing : split) {
            String[] split1 = sing.split(":");
            InfoVO infoVO = new InfoVO();
            infoVO.setName(split1[0]);
            infoVO.setValue(Integer.parseInt(split1[1]));
            cur.add(infoVO);
        }

        if (HistoryInfo.historyInfo.size() > 10) {
            HistoryInfo.historyInfo.remove(0);
            HistoryInfo.times.remove(0);
        }
        HistoryInfo.historyInfo.add(cur);
        HistoryInfo.times.add(df.format(new Date()));
        System.out.println(cur);

    }
}