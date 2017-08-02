package com.jshvarts.shoppinglist.lobby.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jshvarts.shoppinglist.common.domain.model.DatabaseConstants;
import com.jshvarts.shoppinglist.common.domain.model.ShoppingList;
import com.jshvarts.shoppinglist.common.domain.model.ShoppingListItem;
import com.jshvarts.shoppinglist.rx.SchedulersFacade;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class ShoppingListViewModel extends ViewModel {

    private final LoadShoppingListUseCase loadShoppingListUseCase;

    private final UpdateShoppingListUseCase updateShoppingListUseCase;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<ShoppingList> liveShoppingList = new MutableLiveData<>();

    ShoppingListViewModel(LoadShoppingListUseCase loadShoppingListUseCase,
                          UpdateShoppingListUseCase updateShoppingListUseCase,
                          SchedulersFacade schedulersFacade) {
        this.loadShoppingListUseCase = loadShoppingListUseCase;
        this.updateShoppingListUseCase = updateShoppingListUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    LiveData<ShoppingList> getShoppingList() {
        return liveShoppingList;
    }

    void loadShoppingList() {
        disposables.add(loadShoppingListUseCase.loadShoppingList(DatabaseConstants.DEFAULT_SHOPPING_LIST_ID)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(shoppingList -> liveShoppingList.setValue(shoppingList),
                        throwable -> Timber.e(throwable))
        );
    }

    void completeShoppingListItem(int index) {
        ShoppingList shoppingList = liveShoppingList.getValue();
        if (shoppingList.getItems().get(index).getCompleted()) {
            // item already completed. trigger UI refresh only.
            liveShoppingList.setValue(shoppingList);
            return;
        }
        ShoppingListItem completedShoppingListItem = shoppingList.getItems().remove(index);
        completedShoppingListItem.setCompleted(true);
        shoppingList.getItems().add(completedShoppingListItem);

        updateShoppingList(shoppingList);
    }

    private void updateShoppingList(ShoppingList shoppingList) {
        disposables.add(updateShoppingListUseCase.updateShoppingList(shoppingList)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(updatedShoppingList -> liveShoppingList.setValue(updatedShoppingList),
                        throwable -> Timber.e(throwable)
                )
        );
    }
}
