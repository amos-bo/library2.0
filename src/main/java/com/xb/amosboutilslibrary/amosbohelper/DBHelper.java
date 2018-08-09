package com.xb.amosboutilslibrary.amosbohelper;


import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.List;


/**
 * 数据库封装工具类
 * Created by amosbo on 15/8/17.
 */
public class DBHelper<T extends Model> {


    private Where<T> where;

    /**
     * 得到一个DBHelper实例
     *
     * @param isDel 是否是删除
     * @param cls   对应的表
     * @param <T>   Class
     * @return Model
     */
    public static <T extends Model> DBHelper getInstance(boolean isDel, Class<T> cls) {

        return new DBHelper<T>(isDel, cls);

    }

    /**
     * @param isDel true用删除语句，不然用查询
     * @param cls   Class
     */
    public DBHelper(boolean isDel, Class<T> cls) {

        if (isDel) {
            where = new Delete().from(cls).where();
        } else {
            where = new Select().from(cls).where();
        }


    }


    /**
     * 异步存储一项
     *
     * @param model BaseModel
     */
    public static void saveItem(BaseModel model) {

        try {
            model.async().save();
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    /**
     * 同步存储一项
     *
     * @param model BaseModel
     */
    public static void saveSynchronousItem(BaseModel model) {

        try {
            model.save();
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    /**
     * 插入一条数据
     *
     * @param model BaseModel
     */
    public static void insert(BaseModel model) {

        try {
            model.async().insert();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 同步删除一项
     *
     * @param model BaseModel
     */
    public static void deleteSynchronousItem(BaseModel model) {

        try {
            model.delete();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一项
     *
     * @param model BaseModel
     */
    public static void deleteItem(BaseModel model) {

        try {
            model.async().delete();
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    /**
     * 更新一项
     *
     * @param model BaseModel
     */
    public static void updateItem(BaseModel model) {

        try {
            model.async().update();
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }


    /**
     * 表里的记录个数
     *
     * @param cls Class
     * @param <T> Model
     * @return Model
     */
    public static <T extends Model> long count(Class<T> cls) {


        long count = 0;
        try {
            count = SQLite.selectCountOf().from(Model.class).count();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    /**
     * 存储列表
     *
     * @param list List
     * @param <T>  Model
     */
    public static <T extends Model> void saveAll(List<T> list) {


        if (list == null || list.isEmpty()) {
            return;
        }
        try {
            FlowManager.getDatabase(Model.class)
                    .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                            new ProcessModelTransaction.ProcessModel<Model>() {
                                @Override
                                public void processModel(Model model) {
                                    model.save();
                                }
                            }).addAll(list).build())
                    .error(new Transaction.Error() {
                        @Override
                        public void onError(Transaction transaction, Throwable error) {
                            Log.e(DBHelper.class.getName(), error.toString());
                        }
                    })
                    .success(new Transaction.Success() {
                        @Override
                        public void onSuccess(Transaction transaction) {

                        }
                    }).build().execute();

        } catch (Throwable e) {

            e.printStackTrace();
        }


    }


    /**
     * 更新列表
     *
     * @param list List
     * @param cls  Class
     * @param <T>  Model
     */
    public static <T extends Model> void updateAll(List<T> list, Class<T> cls) {


        for (T t : list) {

            t.update();
        }

    }


    /**
     * 查找一个表
     *
     * @param cls Class
     * @param <T> Model
     * @return Model
     */
    public static <T extends Model> List<T> findAll(Class<T> cls) {

        return new Select().from(cls).queryList();


    }


    /**
     * 删除一张表的所有记录
     *
     * @param cls Class
     * @param <T> Model
     */
    public static <T extends Model> void deleteAll(Class<T> cls) {

        Delete.table(cls);
    }


    /**
     * 根据一个条件删除一个list
     *
     * @param cls       Class
     * @param condition Condition
     * @param <T>       Model
     */
    public static <T extends Model> void deleteList(Class<T> cls, Condition condition) {

        Delete.table(cls, condition);


    }


    /**
     * 等于
     *
     * @param isOR      true的时候使用or连接条件，false时用and，
     * @param condition Condition
     * @return DBHelper
     */
    public DBHelper is(boolean isOR, Condition condition) {

        if (isOR) {
            where = where.or(condition);
        } else {
            where = where.and(condition);
        }

        return this;

    }

    /**
     * 大于
     *
     * @param isOR      boolean
     * @param condition Condition
     * @return DBHelper
     */
    public DBHelper greaterThan(boolean isOR, Condition condition) {

        if (isOR) {
            where = where.or(condition);
        } else {
            where = where.and(condition);
        }

        return this;

    }


    /**
     * 小于
     *
     * @param isOR      boolean
     * @param condition Condition
     * @return DBHelper
     */
    public DBHelper lessThan(boolean isOR, Condition condition) {

        if (isOR) {
            where = where.or(condition);
        } else {
            where = where.and(condition);
        }

        return this;

    }


    /**
     * 排序   HistroyTicketBean$Table.TIME + " DESC"
     *
     * @param order String
     * @return DBHelper
     */
    public DBHelper orderBy(String order) {

        where = where.orderBy(OrderBy.fromString(order));

        return this;

    }

    /**
     * 排序
     *
     * @param ascending boolean
     * @return DBHelper
     */
    public DBHelper orderBy(IProperty iProperty, boolean ascending) {

        where = where.orderBy(iProperty, ascending);

        return this;

    }

    /**
     * 限制数量
     *
     * @param pageSize 限制
     * @return DBHelper
     */
    public DBHelper limit(int pageSize) {

        where = where.limit(pageSize);

        return this;
    }


    /**
     * 偏移数量
     *
     * @param offSet 偏移
     * @return DBHelper
     */
    public DBHelper offset(int offSet) {

        where = where.offset(offSet);

        return this;
    }

    /**
     * 得到一个列表
     *
     * @return List
     */
    public List<T> list() {

        try {
            return where.queryList();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 得到第一个
     *
     * @return TModel
     */
    public T one() {


        try {
            return where.querySingle();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }


    public void execute() {

        try {
            where.execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


}
