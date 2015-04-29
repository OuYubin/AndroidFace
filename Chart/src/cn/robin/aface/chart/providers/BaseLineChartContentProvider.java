package cn.robin.aface.chart.providers;

import cn.robin.aface.chart.model.ChartData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robin on 15-3-28.
 * 线性图表内容提供器
 */
public class BaseLineChartContentProvider implements ILineChartContentProvider {

    protected Object referenceObject;

    public BaseLineChartContentProvider() {

    }

    public int getXAxisCount(Object[] objects) {
        int xAxisCount = 0;
        for (Object object : objects) {
            if (object instanceof ChartData) {
                ChartData chartData = (ChartData) object;
                int currentAxisCount = chartData.getChartEntries().size();
                if (currentAxisCount > xAxisCount) {
                    xAxisCount = currentAxisCount;
                    referenceObject = chartData;
                }
            }
        }
        return xAxisCount;
    }


    //--指定X坐标间距系数
    public int getXAxisModulus(Object[] objects) {
        return 3;
    }

    public int getYAxisCount(Object[] objects) {
        return 5;
    }

    public List<String> getXVals(Object[] objects) {
        List<String> list = new ArrayList<String>();
        int count = getXAxisCount(objects);
        if (referenceObject instanceof ChartData) {
            for (int i = 0; i < count; i++) {
                list.add(i + "");
            }
            return list;
        }
        return list;
    }


}
