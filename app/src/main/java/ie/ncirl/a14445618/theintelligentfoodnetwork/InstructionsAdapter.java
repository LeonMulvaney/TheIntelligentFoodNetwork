package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leon on 24/02/2018.
 */

public class InstructionsAdapter extends BaseAdapter {
    Context context;
    ArrayList<InstructionModel> instructionList;
    LayoutInflater inflter;

    public InstructionsAdapter(Context applicationContext, ArrayList<InstructionModel> instructionList) {
        this.context = applicationContext;
        this.instructionList = instructionList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return instructionList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.instructions_adapter_layout, null);

        // Lookup view for data population
        TextView tvInstructionNumber = view.findViewById(R.id.instructionNumberTv);
        TextView tvInstruction = view.findViewById(R.id.instructionTv);


        // Populate the data into the template view using the data object
        tvInstructionNumber.setText(instructionList.get(i).getStepNumber());
        tvInstruction.setText(instructionList.get(i).getInstruction());

        return view;
    }
}
