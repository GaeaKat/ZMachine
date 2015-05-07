package com.nekokittygames.zmachine.blorb.IFF.Chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.IFF.Chunk;
import com.nekokittygames.zmachine.blorb.IFF.Utils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Katrina on 09/02/2015.
 */
public class RIdxChunk extends Chunk {
    public class Resource
    {
        public String Usage;
        public int Number;
        public int Start;
        public Resource(String Usage,int Number,int Start)
        {
            this.Usage=Usage;
            this.Number=Number;
            this.Start=Start;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("Usage",Usage).append("Number",Number).append("Starts",Start).toString();
        }
    }
    protected int num;
    protected List<Resource> resources=new ArrayList<Resource>();

    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);
        num=stream.readInt();
        for(int i=0;i<num;i++)
        {
            byte[] chars=new byte[4];
            stream.read(chars);
            String Usage= Utils.getIdentString(chars);
            int Number=stream.readInt();
            int Start=stream.readInt();
            resources.add(new Resource(Usage,Number,Start));
        }
    }
    public List<Resource> getResources()
    {
        return resources;
    }

    public Resource getResource(String type,int number)
    {
        for(Resource res:resources)
        {
            if(res.Usage.equalsIgnoreCase(type) && res.Number==number)
                return res;
        }
        return null;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("num",num).append("resources",resources).toString();
    }
}
