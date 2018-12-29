package com.netcracker.students.BatyrkinAndrew.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.netcracker.students.BatyrkinAndrew.client.services.*;
import com.netcracker.students.BatyrkinAndrew.shared.bean.MyBook;

import java.util.ArrayList;
import java.util.Date;

public class MyLibrary implements EntryPoint {

    private GetTableServiceAsync getTableService = GWT.create(GetTableService.class);
    private static CellTable<MyBook> cellTable;
    private static ArrayList<MyBook> myBookList = new ArrayList<>();

    private VerticalPanel vpMain = new VerticalPanel();
    private HorizontalPanel hpAdded = new HorizontalPanel();
    private Button bAdd = new Button("Add");
    private Button bDelete = new Button("Delete");
    private Button sortByTitle = new Button("Sort by Title");

    private TextBox tbId = new TextBox();
    private TextBox tbAuthor = new TextBox();
    private TextBox tbTitle = new TextBox();
    private TextBox tbPageAmount = new TextBox();
    private TextBox tbDateOfPublic = new TextBox();


    @Override
    public void onModuleLoad() {
        getTableService.getListOfBook(new TableCallBack());

        bAdd.addClickHandler(event -> {
            int id = Integer.parseInt(tbId.getText());
            String author = tbAuthor.getText();
            String title = tbTitle.getText();
            int pageAmount = Integer.parseInt(tbPageAmount.getText());
            String deteOfPub = tbDateOfPublic.getText();
            final MyBook newMyBook = new MyBook(id, author, title, pageAmount, deteOfPub, new Date());

            AddNewBookServiceAsync addNewBookService = GWT.create(AddNewBookService.class);
            addNewBookService.addNewBook(newMyBook, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Что-то пошло не так!");
                }

                @Override
                public void onSuccess(Void result) {
                    myBookList.add(newMyBook);
                    cellTable.removeFromParent();
                    createTable();
                    createPanel();
                }
            });
        });

        bDelete.addClickHandler(event -> {
            Window.alert("Для удаления выберите книгу!");
            SingleSelectionModel<MyBook> selectionModel = new SingleSelectionModel<>();
            cellTable.setSelectionModel(selectionModel);
            selectionModel.addSelectionChangeHandler(event1 -> {
                MyBook selectedMyBook = selectionModel.getSelectedObject();
                if (selectedMyBook != null) {
                    final MyBook deletedMyBook = selectedMyBook;
                    DeleteBookServiceAsync deleteBookService = GWT.create((DeleteBookService.class));
                    deleteBookService.deleteBook(deletedMyBook, new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Что-то пошло не так!");
                        }

                        @Override
                        public void onSuccess(Void result) {
                            myBookList.remove(deletedMyBook);
                            cellTable.removeFromParent();
                            createTable();
                            createPanel();
                        }
                    });
                }
            });
        });

        sortByTitle.addClickHandler(event -> {
            SortByTitleServiceAsync sortByTitleService = GWT.create(SortByTitleService.class);
            sortByTitleService.sortByTitle(myBookList, new AsyncCallback<ArrayList<MyBook>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Что-то пошло не так!");
                }

                @Override
                public void onSuccess(ArrayList<MyBook> result) {
                    cellTable.removeFromParent();
                    myBookList = result;
                    createTable();
                    createPanel();
                }
            });
        });
    }

    private void createTable() {
        cellTable = new CellTable<>(myBookList.size());

        addIdColumn(cellTable);
        addAuthorColumn(cellTable);
        addTitle(cellTable);
        addPageAmountColumn(cellTable);
        addDateOfPublicColumn(cellTable);
        addDateOfAddedColumn(cellTable);

        cellTable.setWidth("100%");

        cellTable.setRowCount(myBookList.size(), true);
        cellTable.setRowData(0, myBookList);
    }

    private void addDateOfAddedColumn(CellTable<MyBook> cellTable) {
        TextColumn<MyBook> dateOfAdded = new TextColumn<MyBook>() {
            @Override
            public String getValue(MyBook object) { return String.valueOf(object.getDateOfAdded()); }
        };
        cellTable.addColumn(dateOfAdded, "Дата добавления");
    }

    private void addDateOfPublicColumn(CellTable<MyBook> cellTable) {
        TextColumn<MyBook> dateOfPublic = new TextColumn<MyBook>() {
            @Override
            public String getValue(MyBook object) {
                return String.valueOf(object.getDateOfPublication());
            }
        };
        cellTable.addColumn(dateOfPublic, "Дата публикации");
    }

    private void addPageAmountColumn(CellTable<MyBook> cellTable) {
        TextColumn<MyBook> pageAmount = new TextColumn<MyBook>() {
            @Override
            public String getValue(MyBook object) {
                return String.valueOf(object.getPageAmount());
            }
        };
        cellTable.addColumn(pageAmount, "Количество страниц");
    }

    private void addTitle(CellTable<MyBook> cellTable) {
        TextColumn<MyBook> title = new TextColumn<MyBook>() {
            @Override
            public String getValue(MyBook object) {
                return String.valueOf(object.getTitle());
            }
        };
        cellTable.addColumn(title, "Название");
    }

    private void addAuthorColumn(CellTable<MyBook> cellTable) {
        TextColumn<MyBook> author = new TextColumn<MyBook>() {
            @Override
            public String getValue(MyBook object) {
                return String.valueOf(object.getAuthor());
            }
        };
        cellTable.addColumn(author, "Автор");
    }

    private void addIdColumn(CellTable<MyBook> cellTable) {
        TextColumn<MyBook> id = new TextColumn<MyBook>() {
            @Override
            public String getValue(MyBook object) {
                return String.valueOf(object.getId());
            }
        };
        cellTable.addColumn(id, "ID");
    }

    private void createPanel() {
        tbId.setText("Id");
        tbAuthor.setText("Автор");
        tbTitle.setText("Название");
        tbPageAmount.setText("Количсетво страниц");
        tbDateOfPublic.setText("Дата публикации");

        hpAdded.add(tbId);
        hpAdded.add(tbAuthor);
        hpAdded.add(tbTitle);
        hpAdded.add(tbPageAmount);
        hpAdded.add(tbDateOfPublic);
        hpAdded.add(bAdd);
        hpAdded.add(bDelete);
        hpAdded.add(sortByTitle);

        vpMain.add(cellTable);
        vpMain.add(hpAdded);
    }


    class TableCallBack implements AsyncCallback<ArrayList<MyBook>> {
        @Override
        public void onFailure(Throwable caught) {
            Window.alert("Сервер недоступен: " + caught.getMessage());
        }

        @Override
        public void onSuccess(ArrayList<MyBook> result) {
            myBookList = result;
            createTable();
            createPanel();
        }
    }

}
