CREATE PROC [at].[assignStatusAlias]
@aliasNew nvarchar (100) = null,
@aliasScreened nvarchar (100) = null,
@aliasEnrolled nvarchar (100) = null,
@aliasCompleted nvarchar (100)= null,
@aliasDiscontinued nvarchar (100)= null
AS
BEGIN
DECLARE @studyName nvarchar (40) = 'Subject',
		@studyID uniqueidentifier

select @studyID = StudyID FROM [Study] WHERE [Name]=@studyName

DELETE FROM [SubjectStatusAlias] WHERE StudyID=@studyID;

IF @aliasNew IS NOT NULL
INSERT INTO [SubjectStatusAlias] VALUES (NEWID(),'2278259E-08A0-4C8E-8212-6DB62AC189EA',@studyID,'en-us',@aliasNew)

IF @aliasScreened IS NOT NULL
INSERT INTO [SubjectStatusAlias] VALUES (NEWID(),'C239B1B2-907A-45AC-8F6D-4FE0E3AB48B7',@studyID,'en-us',@aliasScreened)

IF @aliasEnrolled IS NOT NULL
INSERT INTO [SubjectStatusAlias] VALUES (NEWID(),'452CA00D-C279-4CF1-83EF-D346E4BDE353',@studyID,'en-us',@aliasEnrolled)

IF @aliasCompleted IS NOT NULL
INSERT INTO [SubjectStatusAlias] VALUES (NEWID(),'8632A5AF-E7A2-4D9A-905E-F96375C9E831',@studyID,'en-us',@aliasCompleted)

IF @aliasDiscontinued IS NOT NULL
INSERT INTO [SubjectStatusAlias] VALUES (NEWID(),'B7061EF4-3EEC-4073-AD29-772EF8B6DBEE',@studyID,'en-us',@aliasDiscontinued)

END