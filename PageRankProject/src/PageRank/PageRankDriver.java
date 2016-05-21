package PageRank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PageRankDriver extends Configured implements Tool {

    private static NumberFormat nf = new DecimalFormat("00");

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Configuration(), new PageRankDriver(), args));
    }

    @Override
    public int run(String[] args) throws Exception {
    	if (args.length != 2) {
    	      System.out.printf("Usage: PageRank <input dir> <output dir>\n");
    	      return -1;
    	    }
    	boolean isCompleted = false;

        String lastResultPath = null;
        
        isCompleted = runPageRankCalculation(args[0], args[1] + "/runs00");

        if (!isCompleted) return 1;

        for (int runs = 0; runs < 4; runs++) {
            String inPath = args[1] + "/runs" + nf.format(runs);
            lastResultPath = args[1] + "/runs" + nf.format(runs + 1);

            isCompleted = runPageRankCalculation(inPath, lastResultPath);

            if (!isCompleted) return 1;
        }

        if (!isCompleted) return 1;
        return 0;
    }

    private boolean runPageRankCalculation(String inputPath, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        //getConf()
        Job pageRankCalculation = Job.getInstance(conf, "PageRankCalculation");
        pageRankCalculation.setJarByClass(PageRankDriver.class);

        pageRankCalculation.setOutputKeyClass(Text.class);
        pageRankCalculation.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(pageRankCalculation, new Path(inputPath));
        FileOutputFormat.setOutputPath(pageRankCalculation, new Path(outputPath));

        pageRankCalculation.setMapperClass(PageRankMapper.class);
        pageRankCalculation.setReducerClass(PageRankReduce.class);

        return pageRankCalculation.waitForCompletion(true);
    }

}
