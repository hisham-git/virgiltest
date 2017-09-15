create proc [at].[deleteSubject]
@screeningName nvarchar(20) 

as 
BEGIN
    SET NOCOUNT ON;
DECLARE @TempSubject TABLE
(
   SubjectId uniqueidentifier,
   SubjectVisitId uniqueidentifier,
   SubjectVisitTaskId uniqueidentifier,
   SubjectVisitFormInstanceId uniqueidentifier
);
INSERT INTO 
    @TempSubject 
SELECT  s.subjectId
			,sv.SubjectVisitID
			,svt.SubjectVisitTaskID
			,SubjectVisitFormInstanceID 
			FROM subject AS s 
			left JOIN subjectVisit AS sv ON sv.SubjectID = s.SubjectID 
			left JOIN SubjectVisitTask AS svt ON svt.SubjectVisitID = sv.SubjectVisitID 
			left JOIN SubjectVisitFormInstance AS svfi ON svt.SubjectVisitID = svfi.SubjectVisitID 
			WHERE s.ScreeningNumber = @screeningName;
begin tran
DELETE FROM SubjectVisitFormInstance WHERE SubjectVisitFormInstanceID IN (SELECT DISTINCT SubjectVisitFormInstanceID from @TempSubject)
DELETE FROM SubjectVisitTask WHERE SubjectVisitTaskID IN (SELECT DISTINCT SubjectVisitTaskID from @TempSubject)
DELETE FROM SubjectVisit WHERE SubjectVisitID IN (SELECT DISTINCT SubjectVisitID from @TempSubject)
DELETE FROM subject WHERE SubjectID IN (SELECT DISTINCT SubjectID from @TempSubject)
commit tran
end