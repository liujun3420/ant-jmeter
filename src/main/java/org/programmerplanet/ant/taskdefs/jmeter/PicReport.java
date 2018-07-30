package org.programmerplanet.ant.taskdefs.jmeter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA. User: jiaou Date: 14-5-5 Time: 下午11:05 To change
 * this template use File | Settings | File Templates.
 */
public class PicReport extends ServletUtilities {
	private String fileName;

	private String titleName;

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 饼状图
	 */
	public static void pieChart3D(DefaultPieDataset dataset, String fileName, String titleName) {
		JFreeChart chart = ChartFactory.createPieChart(titleName, dataset, true, true, false);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint("FailCase", Color.RED);
		plot.setSectionPaint("PassCase", Color.green);
		// 设置分裂效果,需要指定分裂出去的key
		plot.setExplodePercent("FailCase", 0.1);

		// 图片中显示百分比:默认方式
		// plot.setLabelGenerator(new
		// StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));
		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}({2})", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})",
				NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		// 设置背景色为白色
		chart.setBackgroundPaint(Color.white);
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(1.0f);
		// 指定显示的饼图上圆形(true),椭圆形(false)
		plot.setCircular(true);
		// 设置图标题的字体
		Font font = new Font("黑体", Font.CENTER_BASELINE, 20);
		TextTitle title = new TextTitle(titleName);
		title.setFont(font);
		chart.setTitle(title);
		plot.setLabelFont(new Font("SimSun", 0, 15));
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("宋体", Font.BOLD, 16));

		try {
			ChartUtilities.saveChartAsJPEG(
					new File(fileName), // 输出到哪个输出流
					1, // JPEG图片的质量，0~1之间
					chart, // 统计图标对象
					800, // 宽
					400, // 宽
					null // ChartRenderingInfo 信息
			);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void save(DefaultPieDataset dataset, String fileName, String titleName) {
		pieChart3D(dataset, fileName, titleName);
	}
}
