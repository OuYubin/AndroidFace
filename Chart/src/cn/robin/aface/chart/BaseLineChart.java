package cn.robin.aface.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import cn.robin.aface.chart.adapter.ChartAdapter;
import cn.robin.aface.chart.axis.XAxis;
import cn.robin.aface.chart.axis.YAxis;
import cn.robin.aface.chart.component.LineChartComponent;
import cn.robin.aface.chart.component.XAxisComponent;
import cn.robin.aface.chart.component.YAxisComponent;
import cn.robin.aface.chart.utils.Transformer;
import cn.robin.aface.chart.utils.ViewPortManager;
import cn.robin.aface.chart.view.IBaseChartView;
import cn.robin.aface.core.runtime.IAdaptable;

import java.util.List;

/**
 * Created by robin on 15-3-21.
 */
public class BaseLineChart extends BaseChart {

    private YAxis mYAxis;

    private XAxis mXAxis;

    public BaseLineChart() {
        mYAxis = new YAxis();
        mXAxis = new XAxis();
    }

    //--图表元件数据填充
    public void setContent(IAdaptable adapter) {
        LineChartComponent lineChartComponent = new LineChartComponent(adapter);
        this.setComponent(lineChartComponent);

        XAxisComponent xAxisComponent = new XAxisComponent(adapter);
        mXAxis.setComponent(xAxisComponent);

        YAxisComponent yAxisComponent = new YAxisComponent(adapter);
        mYAxis.setComponent(yAxisComponent);
    }


    @Override
    public void paintComponent(Canvas canvas) {
        drawXAxis(canvas);
        drawYAxis(canvas);
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mChartComponent.getViewPortManager().getChartContentRect());
        drawData(canvas);
        canvas.restoreToCount(clipRestoreCount);
    }

    public void drawXAxis(Canvas canvas) {
        mXAxis.paintComponent(canvas);
    }

    public void drawYAxis(Canvas canvas) {
        mYAxis.paintComponent(canvas);
    }

    private void drawData(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        ChartAdapter chartAdapter = (ChartAdapter) mChartComponent.getAdapter(ChartAdapter.class);
        ViewPortManager viewPortManager = mChartComponent.getViewPortManager();
        List entries = chartAdapter.getEntries();

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setSubpixelText(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.rgb(0, 144, 255));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2.0f);

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setAntiAlias(true);
        circlePaint.setSubpixelText(true);
        circlePaint.setFilterBitmap(true);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(2.0f);

        for (int i = 0; i < entries.size(); i++) {
            float[] pts = (float[]) entries.get(i);
            Transformer transformer = mChartComponent.getTransformer();
            transformer.pointValuesToPixel(pts);

            if (i > 0) {
                mPaint.setColor(Color.rgb(255, 148, 0));
            }

            for (int j = 0; j < pts.length; j += 2) {

                if (j != 0 && viewPortManager.isOffContentLeft(pts[j - 1])
                        && viewPortManager.isOffContentTop(pts[j + 1])
                        && viewPortManager.isOffContentBottom(pts[j + 1]))
                    continue;
                canvas.drawCircle(pts[j], pts[j + 1], 7.0f, circlePaint);
                canvas.drawCircle(pts[j], pts[j + 1], 5.0f, mPaint);

                if (j + 3 > pts.length) {
                    break;
                }


                canvas.drawLine(pts[j], pts[j + 1], pts[j + 2], pts[j + 3], mPaint);
            }
        }

    }

    public YAxis getYAxis() {
        return mYAxis;
    }

    public XAxis getXAxis() {
        return mXAxis;
    }
}
