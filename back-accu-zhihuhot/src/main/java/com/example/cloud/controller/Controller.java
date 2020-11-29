package com.example.cloud.controller;

import com.example.cloud.HistoryInfo;
import com.example.cloud.InfoVO;
import com.example.cloud.ResponseVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class Controller {

    @GetMapping("/getChart")
    public ResponseVO getChart() {
        List<List<InfoVO>> historyInfo = new ArrayList<>(HistoryInfo.historyInfo);
        List<String> times = new ArrayList<>(HistoryInfo.times);


        List<InfoVO> last = historyInfo.get(historyInfo.size()-1);
        String name_0 = last.get(0).getName();
        String name_1 = last.get(1).getName();
        String name_2 = last.get(2).getName();

        List<Integer> values_0 = new ArrayList<>();
        List<Integer> values_1 = new ArrayList<>();
        List<Integer> values_2 = new ArrayList<>();

//        for (List<InfoVO> li : historyInfo) {
        for (int i = 0; i < historyInfo.size(); i++) {
            List<InfoVO> li = historyInfo.get(i);
            values_0.add(0);
            values_1.add(0);
            values_2.add(0);

            for (InfoVO vo : li) {
                if (vo.getName().equals(name_0)) {
                    values_0.set(i, vo.getValue());
                } else if (vo.getName().equals(name_1)) {
                    values_1.set(i, vo.getValue());
                } else if (vo.getName().equals(name_2)) {
                    values_2.set(i, vo.getValue());
                }
            }
        }

        ResponseVO responseVO = new ResponseVO();
        responseVO.setNames(Arrays.asList(name_0, name_1, name_2));
        responseVO.setValues_0(values_0);
        responseVO.setValues_1(values_1);
        responseVO.setValues_2(values_2);
        responseVO.setTimes(times);

        return responseVO;
    }
}
