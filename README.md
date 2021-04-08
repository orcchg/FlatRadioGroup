# FlatRadioGroup
RadioGroup designed to sit inside ConstraintLayout and reference a set of RadioButtons, without wrapping them.
Any `CompoundButton` is appropriate.

Usage:

```
<androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <RadioButton
            android:id="@+id/btn_plot_day"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />

        <RadioButton
            android:id="@+id/btn_plot_week"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

        <com.orcchg.flatradiogroup.design.view.FlatRadioGroup
            android:id="@+id/radio_group"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:constraint_referenced_ids="btn_plot_day,btn_plot_week" />

    </androidx.constraintlayout.widget.ConstraintLayout>
```

There is also an `rxbinding` for `FlatRadioGroup` which emits each time checked status has changed inside that group:

```
val radioGroup: FlatRadioGroup = findViewById(R.id.radio_group)

radioGroup.checkedChanges().skipInitialValue().subscribe { (id, isChecked) ->
    // CompoundButton with id was checked / unchecked
}
```
