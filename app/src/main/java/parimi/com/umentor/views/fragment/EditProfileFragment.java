package parimi.com.umentor.views.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parimi.com.umentor.CheckBoxClickInterface;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.CategoryAdapter;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.Constants;
import parimi.com.umentor.models.Category;
import parimi.com.umentor.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements CheckBoxClickInterface {

    User user;

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.name)
    EditText nameTxt;

    @BindView(R.id.email)
    TextView emailTxt;

    @BindView(R.id.age)
    EditText ageTxt;

    @BindView(R.id.experience)
    EditText experienceTxt;

    @BindView(R.id.saveButton)
    Button saveButton;

    @BindView(R.id.category_list_view)
    GridView listView;

    List<Category> categories = new ArrayList<>();

    List<Category> selectedCategories = new ArrayList<>();
    public  EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.get("user");
            nameTxt.setText(user.getName());
            emailTxt.setText(user.getEmail());
            ageTxt.setText(String.valueOf(user.getAge()));
            experienceTxt.setText(String.valueOf(user.getExperience()));
            selectedCategories.addAll(user.getCategory());

        }
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }

        databaseHelper.getCategories().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categoriesSnapshot: dataSnapshot.getChildren()) {
                    categories.add(new Category(categoriesSnapshot.getValue().toString()));
                }

                CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories, Constants.EDITPROFILEFRAGMENT);
                categoryAdapter.setOnItemSelected(EditProfileFragment.this);
                listView.setAdapter(categoryAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }


    @OnClick(R.id.saveButton)
    public void onSaveButtonClicked() {

        user.setName(nameTxt.getText().toString());
        user.setAge(Integer.parseInt(ageTxt.getText().toString()));
        user.setExperience(Integer.parseInt(experienceTxt.getText().toString()));
        user.setCategory(selectedCategories);
        databaseHelper.saveUser(user);
    }

    @Override
    public void onItemSelected(String name) {
        selectedCategories.add(new Category(name));
    }
}
