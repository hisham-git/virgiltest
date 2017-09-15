package steps.Configuration;

import mt.siteportal.utils.db.DbUtil;
import mt.siteportal.utils.tools.Log;
import steps.Abstract.AbstractStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maksym.tkachov on 6/27/2016.
 */
public class DBSteps extends AbstractStep {

    private static final String DELETESUBJECTVISIT = "[at].[deleteSubjectVisit]";
    private static final String ASSIGNSTATUSALIAS = "[at].[assignStatusAlias]";
    private static final String DELETEOBSERVER = "[at].[deleteSubjectObserver]";
    private static final String DELETESUBJECT="[at].[deleteSubject] ";

    public void deleteSubjectVisit(String subjectName,String visitName ) {
        Log.logInfo("Deleting data from DB of SubjectVisit entity");
        List<String> lst = new ArrayList<String>();
        lst.add(subjectName);
        lst.add(visitName);
        DbUtil.callProcedure(DELETESUBJECTVISIT, lst);
    }

    public void insertAssignStatusAliases(String [] aliases){
        Log.logInfo("Setting the custom alias for Statuses with DB");
        List<String> lst = Arrays.asList(aliases);
        DbUtil.callProcedure(ASSIGNSTATUSALIAS, lst);
    }

    public void deleteObserver(String subjectName, String relation, String alias){
        List<String> deleteObserver = new ArrayList<String>();
        deleteObserver.add(subjectName);
        deleteObserver.add(relation);
        deleteObserver.add(alias);
        DbUtil.callProcedure(DELETEOBSERVER, deleteObserver);
        deleteObserver.clear();
    }

    public void deleteSubject(String subjectName){
        Log.logInfo("Deleting data from DB of Subject entity");
        List<String> lst = new ArrayList<String>();
        lst.add(subjectName);
        DbUtil.callProcedure(DELETESUBJECT, lst);
    }

    public void insertStudySubjectField(String user, String study, String field){
        List<String> subjectFieldVisibility = new ArrayList<String>();
        subjectFieldVisibility.add(field);
        subjectFieldVisibility.add(study);
        subjectFieldVisibility.add(user);
        DbUtil.callProcedure("[at].[setSubjectFieldVisibility]",subjectFieldVisibility);
    }

    public void deleteStudySubjectField(){
        DbUtil.callProcedure("[at].[setSubjectFieldVisibility]",new ArrayList<String>());
    }
}
