package PageRank;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PageRankMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        int pageTabIndex = value.find("\t");
        int rankTabIndex = value.find("\t", pageTabIndex+1);
    	String line = value.toString();
    	String[] values = line.split("\t");
    	

        //String page = Text.decode(value.getBytes(), 0, pageTabIndex);
        String pageWithRank = "1";
        
        //context.write(new Text(page), new Text("!"));

        // Skip pages with no links.
        if(rankTabIndex == -1) return;
        
       // String links = Text.decode(value.getBytes(), rankTabIndex+1, value.getLength()-(rankTabIndex+1));
        //String[] allOtherPages = links.split(",");
        
        String from_journal = values[0];
        String to_journal = values[1];
        String totalLinks = values[4];
        String citations = values[2];
        String journal_size = values[3];
        
        //for (String otherPage : allOtherPages){
        Text pageRankTotalLinks = new Text(pageWithRank + "\t" + citations + "\t" + journal_size + "\t" + totalLinks);
//        context.write(new Text(to_journal), pageRankTotalLinks);
        //}
        
        // Put the original links of the page for the reduce output
        context.write(new Text(from_journal), new Text(pageRankTotalLinks));
    }
}
