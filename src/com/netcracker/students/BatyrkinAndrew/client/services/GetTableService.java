package com.netcracker.students.BatyrkinAndrew.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.netcracker.students.BatyrkinAndrew.shared.bean.MyBook;

import java.util.ArrayList;

@RemoteServiceRelativePath("GetTableService")
public interface GetTableService extends RemoteService {
    ArrayList<MyBook> getListOfBook();

}
