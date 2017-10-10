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
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import parimi.com.umentor.CheckBoxClickInterface;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.CategoryAdapter;
import parimi.com.umentor.application.UMentorDaggerInjector;
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

    @BindView(R.id.expertiseEditText)
    EditText expertiseTxt;

    @BindView(R.id.saveButton)
    Button saveButton;

    @BindView(R.id.category_list_view)
    GridView listView;

    List<Category> categories = new ArrayList<>();

    List<String> selectedCategories = new ArrayList<>();

    HashMap<String, Boolean> saveSelectedCategories = new HashMap<>();

    public  EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UMentorDaggerInjector.get().inject(this);
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.get("user");
            nameTxt.setText(user.getName());
            emailTxt.setText(user.getEmail());
            ageTxt.setText(String.valueOf(user.getAge()));
            experienceTxt.setText(String.valueOf(user.getExperience()));
            expertiseTxt.setText(user.getSummary());
        }

        databaseHelper.getCategories().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categoriesSnapshot: dataSnapshot.getChildren()) {
                    categories.add(new Category(categoriesSnapshot.getValue().toString()));
                }

                CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories, Constants.EDITPROFILEFRAGMENT);
                categoryAdapter.setOnChechboxItemSelected(EditProfileFragment.this);
                listView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @OnTextChanged({R.id.name, R.id.age, R.id.experience, R.id.expertiseEditText})
    public void fieldsChanged() {
        enableDisableSaveButton();
    }


    public void enableDisableSaveButton() {
        if(nameTxt.getText().length() > 0 &&
                Integer.parseInt(ageTxt.getText().toString()) > 10 &&
                experienceTxt.getText().length() > 0 &&
                expertiseTxt.getText().length() > 0 &&
                selectedCategories.size() > 0) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }

    }


    @OnClick(R.id.saveButton)
    public void onSaveButtonClicked() {

        user.setName(nameTxt.getText().toString());
        user.setAge(Integer.parseInt(ageTxt.getText().toString()));
        user.setExperience(Integer.parseInt(experienceTxt.getText().toString()));
        user.setSummary(expertiseTxt.getText().toString());
        databaseHelper.saveUser(user);

        for(int i=0;i < selectedCategories.size();i++) {
            saveSelectedCategories.put(user.getId(), true);
            databaseHelper.saveUserToCategories(selectedCategories.get(i), user.getId());
        }
    }

    @Override
    public void onItemSelected(String name) {
        selectedCategories.add(name);
        enableDisableSaveButton();
    }
}
