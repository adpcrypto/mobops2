package com.tensors.sahyognew.databinding;
import com.tensors.sahyognew.R;
import com.tensors.sahyognew.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityLoginBindingImpl extends ActivityLoginBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.login, 1);
        sViewsWithIds.put(R.id.loading, 2);
        sViewsWithIds.put(R.id.LoginHeading, 3);
        sViewsWithIds.put(R.id.LoginSubheading, 4);
        sViewsWithIds.put(R.id.usernameLayout, 5);
        sViewsWithIds.put(R.id.username, 6);
        sViewsWithIds.put(R.id.passwordLayout, 7);
        sViewsWithIds.put(R.id.password, 8);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityLoginBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private ActivityLoginBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[4]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (android.widget.ProgressBar) bindings[2]
            , (android.widget.Button) bindings[1]
            , (com.google.android.material.textfield.TextInputEditText) bindings[8]
            , (com.google.android.material.textfield.TextInputLayout) bindings[7]
            , (com.google.android.material.textfield.TextInputEditText) bindings[6]
            , (com.google.android.material.textfield.TextInputLayout) bindings[5]
            );
        this.container.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}