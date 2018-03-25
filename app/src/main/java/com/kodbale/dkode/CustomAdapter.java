package com.kodbale.dkode;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kodbale.dkode.database.Question;
import java.util.ArrayList;

/**
 * This class contans the adapter for the custom list view
 */

public class CustomAdapter  extends ArrayAdapter<Question>{

    private Activity context;
    private int number;
    ArrayList<Question> questions;
    public CustomAdapter(Activity context, ArrayList<Question> questionss, int resource) {
        super(context, resource, questionss);
        this.context = context;
        this.questions = questionss;
        this.number = 1;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder = null;
        if (v == null){
            v = context.getLayoutInflater().inflate(R.layout.entry,parent,false);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        Question question = questions.get(position);
        if (question==null) return v;
        String id = "" +this.number+".";
        String scr = "" + question.getScore();
        //viewHolder.qno.setText(id);
        (this.number)++;

        viewHolder.qtitle.setText(question.getQuestionText());
        viewHolder.qscore.setText(scr);
        return v;
    }

    class ViewHolder{
        //TextView qno;
        TextView qtitle;
        TextView qscore;
        ViewHolder(View v){
            //qno=(TextView)v.findViewById(R.id.qno_display);
            qtitle = (TextView)v.findViewById(R.id.qtitle_display);
            qscore = (TextView)v.findViewById(R.id.qscore_display);

        }
    }
}