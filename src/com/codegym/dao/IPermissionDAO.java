package com.codegym.dao;

import com.codegym.model.Permission;

import java.util.List;

public interface IPermissionDAO {
	List<Permission> selectAllPermission();
}
