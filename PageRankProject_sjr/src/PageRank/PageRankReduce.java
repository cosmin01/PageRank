package PageRank;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PageRankReduce extends Reducer<Text, Text, Text, Text> {

    private static final float d_const = 0.85F;
    private static final float e_const = 0.10F;

    @Override
    public void reduce(Text page, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        boolean isExistingWikiPage = false;
        String[] split;
        float sumShareOtherPageRanks = 0;
        String links = "";
        String pageWithRank;
        int N = 3;
        float partOne = 0;
        float partTwo = 0;
        float partTree = 0;
        float partFour = 0;
        
        // For each otherPage: 
        // - check control characters
        // - calculate pageRank share <rank> / count(<links>)
        // - add the share to sumShareOtherPageRanks
        for (Text value : values){
            pageWithRank = value.toString();
            
//            if(pageWithRank.equals("!")) {
//                isExistingWikiPage = true;
//                continue;
//            }
            
//            if(pageWithRank.startsWith("|")){
//                links = "\t"+pageWithRank.substring(1);
//                continue;
//            }

            split = pageWithRank.split("\t");
            
            float pageRank = Float.valueOf(split[0]);
            int countOutLinks = Integer.valueOf(split[3]);
            int journal_size = Integer.valueOf(split[2]);
            int citations = Integer.valueOf(split[1]);
            
//            sumShareOtherPageRanks += (pageRank/countOutLinks);
            
            partOne += (1-d_const - e_const) / N;
            partTwo += e_const * (journal_size / 1);
            partTree += d_const * (citations / countOutLinks);
            partFour += d_const * (journal_size / 1);
        }

        //if(!isExistingWikiPage) return;
        float newRank = partOne + partTwo + partTree + partFour;

        context.write(page, new Text(String.valueOf(newRank)));
    }
}
