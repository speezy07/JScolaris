package controller;

import dao.GradeDao;
import dao.ModuleDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TeacherDao;
import view.DashboardView;

import java.sql.SQLException;

public class DashboardController {
    private final DashboardView view = new DashboardView();
    private final StudentDao studentDao = new StudentDao();
    private final TeacherDao teacherDao = new TeacherDao();
    private final ModuleDao moduleDao = new ModuleDao();
    private final SubjectDao subjectDao = new SubjectDao();
    private final GradeDao gradeDao = new GradeDao();

    public DashboardController() {
        refresh();
    }

    public DashboardView getView() {
        return view;
    }

    public void refresh() {
        try {
            view.setCounts(
                    studentDao.count(),
                    teacherDao.count(),
                    moduleDao.count(),
                    subjectDao.count(),
                    gradeDao.count()
            );
        } catch (SQLException exception) {
            view.showDatabaseUnavailable();
        }
    }
}
