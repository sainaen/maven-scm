package org.apache.maven.scm.provider;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 */
public interface ScmProvider
{
    String ROLE = ScmProvider.class.getName();

    String getScmType();

    /**
     * Add a logger listener.
     *
     * @param logger The logger
     */
    void addListener( ScmLogger logger );

    boolean requiresEditMode();

    ScmProviderRepository makeProviderScmRepository( String scmSpecificUrl, char delimiter )
        throws ScmRepositoryException;

    ScmProviderRepository makeProviderScmRepository( File path )
        throws ScmRepositoryException, UnknownRepositoryStructure;

    /**
     * Validate the scm url.
     *
     * @param scmSpecificUrl The SCM url
     * @param delimiter      The delimiter used in the SCM url
     * @return Returns a list of messages if the validation failed
     */
    List validateScmUrl( String scmSpecificUrl, char delimiter );

    /**
     * Returns the scm reserved file name where the SCM stores information like 'CVS', '.svn'.
     *
     * @return the scm reserved file name
     */
    String getScmSpecificFilename();

    /**
     * Check if this tag is valid for this SCM provider.
     *
     * @param tag tag name to check
     * @return true if tag is valid
     */
    boolean validateTagName( String tag );

    /**
     * Given a tag name, make it suitable for this SCM provider. For example, CVS converts "." into "_"
     *
     * @param tag input tag name
     * @return sanitized tag name
     */
    String sanitizeTagName( String tag );

    /**
     * Adds the given files to the source control system
     *
     * @param repository the source control system
     * @param fileSet    the files to be added
     * @return an {@link AddScmResult} that contains the files that have been added
     * @throws ScmException
     */
    AddScmResult add( ScmRepository repository, ScmFileSet fileSet )
        throws ScmException;

    /**
     * Adds the given files to the source control system
     *
     * @param repository the source control system
     * @param fileSet    the files to be added
     * @param message    a string that is a comment on the new added file
     * @return an {@link AddScmResult} that contains the files that have been added
     * @throws ScmException
     */
    AddScmResult add( ScmRepository repository, ScmFileSet fileSet, String message )
        throws ScmException;

    /**
     * Returns the changes that have happend in the source control system in a certain period of time.
     * This can be adding, removing, updating, ... of files
     *
     * @param repository the source control system
     * @param fileSet    the files to know the changes about. Implementations can also give the changes
     *                   from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param startDate  the start date of the period
     * @param endDate    the end date of the period
     * @param numDays    the number days before the current time if startdate and enddate are null
     * @param branch     the branch/tag name
     * @return The SCM result of the changelog command
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#changeLog(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,java.util.Date,java.util.Date,int,org.apache.maven.scm.ScmBranch)}
     */
    ChangeLogScmResult changeLog( ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate,
                                  int numDays, String branch )
        throws ScmException;

    /**
     * Returns the changes that have happend in the source control system in a certain period of time.
     * This can be adding, removing, updating, ... of files
     *
     * @param repository the source control system
     * @param fileSet    the files to know the changes about. Implementations can also give the changes
     *                   from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param startDate  the start date of the period
     * @param endDate    the end date of the period
     * @param numDays    the number days before the current time if startdate and enddate are null
     * @param branch     the branch/tag
     * @return The SCM result of the changelog command
     * @throws ScmException
     */
    ChangeLogScmResult changeLog( ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate,
                                  int numDays, ScmBranch branch )
        throws ScmException;

    /**
     * Returns the changes that have happend in the source control system in a certain period of time.
     * This can be adding, removing, updating, ... of files
     *
     * @param repository  the source control system
     * @param fileSet     the files to know the changes about. Implementations can also give the changes
     *                    from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param startDate   the start date of the period
     * @param endDate     the end date of the period
     * @param numDays     the number days before the current time if startdate and enddate are null
     * @param branch      the branch/tag name
     * @param datePattern the date pattern use in changelog output returned by scm tool
     * @return The SCM result of the changelog command
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#changeLog(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,java.util.Date,java.util.Date,int,org.apache.maven.scm.ScmBranch,String)}
     */
    ChangeLogScmResult changeLog( ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate,
                                  int numDays, String branch, String datePattern )
        throws ScmException;

    /**
     * Returns the changes that have happend in the source control system in a certain period of time.
     * This can be adding, removing, updating, ... of files
     *
     * @param repository  the source control system
     * @param fileSet     the files to know the changes about. Implementations can also give the changes
     *                    from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param startDate   the start date of the period
     * @param endDate     the end date of the period
     * @param numDays     the number days before the current time if startdate and enddate are null
     * @param branch      the branch/tag
     * @param datePattern the date pattern use in changelog output returned by scm tool
     * @return The SCM result of the changelog command
     * @throws ScmException
     */
    ChangeLogScmResult changeLog( ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate,
                                  int numDays, ScmBranch branch, String datePattern )
        throws ScmException;

    /**
     * Returns the changes that have happend in the source control system between two tags.
     * This can be adding, removing, updating, ... of files
     *
     * @param repository the source control system
     * @param fileSet    the files to know the changes about. Implementations can also give the changes
     *                   from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param startTag   the start tag
     * @param endTag     the end tag
     * @return The SCM result of the changelog command
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#changeLog(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,org.apache.maven.scm.ScmVersion)}
     */
    ChangeLogScmResult changeLog( ScmRepository repository, ScmFileSet fileSet, String startTag, String endTag )
        throws ScmException;

    /**
     * Returns the changes that have happend in the source control system between two tags.
     * This can be adding, removing, updating, ... of files
     *
     * @param repository   the source control system
     * @param fileSet      the files to know the changes about. Implementations can also give the changes
     *                     from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param startVersion the start branch/tag/revision
     * @param endVersion   the end branch/tag/revision
     * @return The SCM result of the changelog command
     * @throws ScmException
     */
    ChangeLogScmResult changeLog( ScmRepository repository, ScmFileSet fileSet, ScmVersion startVersion,
                                  ScmVersion endVersion )
        throws ScmException;

    /**
     * Returns the changes that have happend in the source control system between two tags.
     * This can be adding, removing, updating, ... of files
     *
     * @param repository  the source control system
     * @param fileSet     the files to know the changes about. Implementations can also give the changes
     *                    from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param startTag    the start tag
     * @param endTag      the end tag
     * @param datePattern the date pattern use in changelog output returned by scm tool
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#changeLog(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,org.apache.maven.scm.ScmVersion,String)}
     */
    ChangeLogScmResult changeLog( ScmRepository repository, ScmFileSet fileSet, String startTag, String endTag,
                                  String datePattern )
        throws ScmException;

    /**
     * Returns the changes that have happend in the source control system between two tags.
     * This can be adding, removing, updating, ... of files
     *
     * @param repository    the source control system
     * @param fileSet       the files to know the changes about. Implementations can also give the changes
     *                      from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param startRevision the start revision
     * @param endRevision   the end revision
     * @param datePattern   the date pattern use in changelog output returned by scm tool
     * @return
     * @throws ScmException
     */
    ChangeLogScmResult changeLog( ScmRepository repository, ScmFileSet fileSet, ScmVersion startRevision,
                                  ScmVersion endRevision, String datePattern )
        throws ScmException;

    /**
     * Save the changes you have done into the repository. This will create a new version of the file or
     * directory in the repository.
     * <p/>
     * When the fileSet has no entries, the fileSet.getBaseDir() is recursively committed.
     * When the fileSet has entries, the commit is non-recursive and only the elements in the fileSet
     * are committed.
     *
     * @param repository the source control system
     * @param fileSet    the files to check in (sometimes called commit)
     * @param tag        tag or revision
     * @param message    a string that is a comment on the changes that where done
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#checkIn(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,String)}
     */
    CheckInScmResult checkIn( ScmRepository repository, ScmFileSet fileSet, String tag, String message )
        throws ScmException;

    /**
     * Save the changes you have done into the repository. This will create a new version of the file or
     * directory in the repository.
     * <p/>
     * When the fileSet has no entries, the fileSet.getBaseDir() is recursively committed.
     * When the fileSet has entries, the commit is non-recursive and only the elements in the fileSet
     * are committed.
     *
     * @param repository the source control system
     * @param fileSet    the files to check in (sometimes called commit)
     * @param message    a string that is a comment on the changes that where done
     * @return
     * @throws ScmException
     */
    CheckInScmResult checkIn( ScmRepository repository, ScmFileSet fileSet, String message )
        throws ScmException;

    /**
     * Save the changes you have done into the repository. This will create a new version of the file or
     * directory in the repository.
     * <p/>
     * When the fileSet has no entries, the fileSet.getBaseDir() is recursively committed.
     * When the fileSet has entries, the commit is non-recursive and only the elements in the fileSet
     * are committed.
     *
     * @param repository the source control system
     * @param fileSet    the files to check in (sometimes called commit)
     * @param revision   branch/tag/revision
     * @param message    a string that is a comment on the changes that where done
     * @return
     * @throws ScmException
     */
    CheckInScmResult checkIn( ScmRepository repository, ScmFileSet fileSet, ScmVersion revision, String message )
        throws ScmException;

    /**
     * Create a copy of the repository on your local machine
     *
     * @param repository the source control system
     * @param fileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param tag        get the version defined by the tag
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#checkOut(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion)}
     */
    CheckOutScmResult checkOut( ScmRepository repository, ScmFileSet fileSet, String tag )
        throws ScmException;

    /**
     * Create a copy of the repository on your local machine
     *
     * @param repository the source control system
     * @param fileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @return
     * @throws ScmException
     */
    CheckOutScmResult checkOut( ScmRepository repository, ScmFileSet fileSet )
        throws ScmException;

    /**
     * Create a copy of the repository on your local machine
     *
     * @param repository the source control system
     * @param fileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param version    get the version defined by the revision, branch or tag
     * @return
     * @throws ScmException
     */
    CheckOutScmResult checkOut( ScmRepository repository, ScmFileSet fileSet, ScmVersion version )
        throws ScmException;

    /**
     * Create a copy of the repository on your local machine.
     *
     * @param scmRepository the source control system
     * @param scmFileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param tag           tag or revision
     * @param recursive     whether to check out recursively
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#checkOut(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,boolean)}
     */
    CheckOutScmResult checkOut( ScmRepository scmRepository, ScmFileSet scmFileSet, String tag, boolean recursive )
        throws ScmException;

    /**
     * Create a copy of the repository on your local machine.
     *
     * @param scmRepository the source control system
     * @param scmFileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param recursive     whether to check out recursively
     * @return
     * @throws ScmException
     */
    CheckOutScmResult checkOut( ScmRepository scmRepository, ScmFileSet scmFileSet, boolean recursive )
        throws ScmException;

    /**
     * Create a copy of the repository on your local machine.
     *
     * @param scmRepository the source control system
     * @param scmFileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param version       get the version defined by the revision, branch or tag
     * @param recursive     whether to check out recursively
     * @return
     * @throws ScmException
     */
    CheckOutScmResult checkOut( ScmRepository scmRepository, ScmFileSet scmFileSet, ScmVersion version,
                                boolean recursive )
        throws ScmException;

    /**
     * Create a diff between two branch/tag/revision.
     *
     * @param scmRepository the source control system
     * @param scmFileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param startRevision the start revision
     * @param endRevision   the end revision
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#diff(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,org.apache.maven.scm.ScmVersion)}
     */
    DiffScmResult diff( ScmRepository scmRepository, ScmFileSet scmFileSet, String startRevision, String endRevision )
        throws ScmException;

    /**
     * Create a diff between two branch/tag/revision.
     *
     * @param scmRepository the source control system
     * @param scmFileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param startVersion  the start branch/tag/revision
     * @param endVersion    the end branch/tag/revision
     * @return
     * @throws ScmException
     */
    DiffScmResult diff( ScmRepository scmRepository, ScmFileSet scmFileSet, ScmVersion startVersion,
                        ScmVersion endVersion )
        throws ScmException;

    /**
     * Create an exported copy of the repository on your local machine
     *
     * @param repository the source control system
     * @param fileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param tag        get the version defined by the tag
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#export(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion)}
     */
    ExportScmResult export( ScmRepository repository, ScmFileSet fileSet, String tag )
        throws ScmException;

    /**
     * Create an exported copy of the repository on your local machine
     *
     * @param repository the source control system
     * @param fileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @return
     * @throws ScmException
     */
    ExportScmResult export( ScmRepository repository, ScmFileSet fileSet )
        throws ScmException;

    /**
     * Create an exported copy of the repository on your local machine
     *
     * @param repository the source control system
     * @param fileSet    the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param version    get the version defined by the branch/tag/revision
     * @return
     * @throws ScmException
     */
    ExportScmResult export( ScmRepository repository, ScmFileSet fileSet, ScmVersion version )
        throws ScmException;

    /**
     * Create an exported copy of the repository on your local machine
     *
     * @param repository      the source control system
     * @param fileSet         the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param tag             get the version defined by the tag
     * @param outputDirectory the directory where the export will be stored
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#export(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,String)}
     */
    ExportScmResult export( ScmRepository repository, ScmFileSet fileSet, String tag, String outputDirectory )
        throws ScmException;

    /**
     * Create an exported copy of the repository on your local machine
     *
     * @param repository      the source control system
     * @param fileSet         the files are copied to the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} location
     * @param version         get the version defined by the branch/tag/revision
     * @param outputDirectory the directory where the export will be stored
     * @return
     * @throws ScmException
     */
    ExportScmResult export( ScmRepository repository, ScmFileSet fileSet, ScmVersion version, String outputDirectory )
        throws ScmException;

    /**
     * Removes the given files from the source control system
     *
     * @param repository the source control system
     * @param fileSet    the files to be removed
     * @param message
     * @return
     * @throws ScmException
     */
    RemoveScmResult remove( ScmRepository repository, ScmFileSet fileSet, String message )
        throws ScmException;

    /**
     * Returns the status of the files in the source control system. The state of each file can be one
     * of the {@link org.apache.maven.scm.ScmFileStatus} flags.
     *
     * @param repository the source control system
     * @param fileSet    the files to know the status about. Implementations can also give the changes
     *                   from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @return
     * @throws ScmException
     */
    StatusScmResult status( ScmRepository repository, ScmFileSet fileSet )
        throws ScmException;

    /**
     * Tag (or label in some systems) will tag the source file with a certain tag
     *
     * @param repository the source control system
     * @param fileSet    the files to tag. Implementations can also give the changes
     *                   from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param tagName    the tag name to apply to the files
     * @return
     * @throws ScmException
     */
    TagScmResult tag( ScmRepository repository, ScmFileSet fileSet, String tagName )
        throws ScmException;

    /**
     * Tag (or label in some systems) will tag the source file with a certain tag
     *
     * @param repository the source control system
     * @param fileSet    the files to tag. Implementations can also give the changes
     *                   from the {@link org.apache.maven.scm.ScmFileSet#getBasedir()} downwards.
     * @param tagName    the tag name to apply to the files
     * @param message    the commit message used for the tag creation
     * @return
     * @throws ScmException
     */
    TagScmResult tag( ScmRepository repository, ScmFileSet fileSet, String tagName, String message )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository the source control system
     * @param fileSet    location of your local copy
     * @return
     * @throws ScmException
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository the source control system
     * @param fileSet    location of your local copy
     * @param tag        use the version defined by the tag
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#update(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion)}
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, String tag )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository the source control system
     * @param fileSet    location of your local copy
     * @param version    use the version defined by the branch/tag/revision
     * @return
     * @throws ScmException
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, ScmVersion version )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository   the source control system
     * @param fileSet      location of your local copy
     * @param tag          use the version defined by the tag
     * @param runChangelog Run the changelog command after the update
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#update(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,boolean)}
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, String tag, boolean runChangelog )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository   the source control system
     * @param fileSet      location of your local copy
     * @param runChangelog Run the changelog command after the update
     * @return
     * @throws ScmException
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, boolean runChangelog )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository   the source control system
     * @param fileSet      location of your local copy
     * @param version      use the version defined by the branch/tag/revision
     * @param runChangelog Run the changelog command after the update
     * @return
     * @throws ScmException
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, ScmVersion version, boolean runChangelog )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository  the source control system
     * @param fileSet     location of your local copy
     * @param tag         use the version defined by the tag
     * @param datePattern the date pattern use in changelog output returned by scm tool
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#update(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,String)}
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, String tag, String datePattern )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository  the source control system
     * @param fileSet     location of your local copy
     * @param version     use the version defined by the branch/tag/revision
     * @param datePattern the date pattern use in changelog output returned by scm tool
     * @return
     * @throws ScmException
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, ScmVersion version, String datePattern )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository the source control system
     * @param fileSet    location of your local copy
     * @param tag        use the version defined by the tag
     * @param lastUpdate
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#update(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,java.util.Date)}
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, String tag, Date lastUpdate )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository the source control system
     * @param fileSet    location of your local copy
     * @param version    use the version defined by the branch/tag/revision
     * @param lastUpdate
     * @return
     * @throws ScmException
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, ScmVersion version, Date lastUpdate )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository  the source control system
     * @param fileSet     location of your local copy
     * @param tag         use the version defined by the tag
     * @param lastUpdate  Date of last update
     * @param datePattern the date pattern use in changelog output returned by scm tool
     * @return
     * @throws ScmException
     * @deprecated you must use {@link ScmProvider#update(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,org.apache.maven.scm.ScmVersion,java.util.Date,String)}
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, String tag, Date lastUpdate,
                            String datePattern )
        throws ScmException;

    /**
     * Updates the copy on the local machine with the changes in the repository
     *
     * @param repository  the source control system
     * @param fileSet     location of your local copy
     * @param version     use the version defined by the branch/tag/revision
     * @param lastUpdate  Date of last update
     * @param datePattern the date pattern use in changelog output returned by scm tool
     * @return
     * @throws ScmException
     */
    UpdateScmResult update( ScmRepository repository, ScmFileSet fileSet, ScmVersion version, Date lastUpdate,
                            String datePattern )
        throws ScmException;

    /**
     * Make a file editable. This is used in source control systems where you look at read-only files
     * and you need to make them not read-only anymore before you can edit them. This can also mean
     * that no other user in the system can make the file not read-only anymore.
     *
     * @param repository the source control system
     * @param fileSet    the files to make editable
     * @return
     * @throws ScmException
     */
    EditScmResult edit( ScmRepository repository, ScmFileSet fileSet )
        throws ScmException;

    /**
     * Make a file no longer editable. This is the conterpart of {@link #edit(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet)}.
     * It makes the file read-only again.
     *
     * @param repository the source control system
     * @param fileSet    the files to make uneditable
     * @return
     * @throws ScmException
     */
    UnEditScmResult unedit( ScmRepository repository, ScmFileSet fileSet )
        throws ScmException;

    /**
     * List each element (files and directories) of <B>fileSet</B> as they exist in the repository.
     *
     * @param repository the source control system
     * @param fileSet    the files to list
     * @param recursive  descend recursively
     * @param tag        use the version defined by the tag
     * @return the list of files in the repository
     * @deprecated you must use {@link ScmProvider#list(org.apache.maven.scm.repository.ScmRepository,org.apache.maven.scm.ScmFileSet,boolean,org.apache.maven.scm.ScmVersion)}
     */
    ListScmResult list( ScmRepository repository, ScmFileSet fileSet, boolean recursive, String tag )
        throws ScmException;

    /**
     * List each element (files and directories) of <B>fileSet</B> as they exist in the repository.
     *
     * @param repository the source control system
     * @param fileSet    the files to list
     * @param recursive  descend recursively
     * @param version    use the version defined by the branch/tag/revision
     * @return the list of files in the repository
     */
    ListScmResult list( ScmRepository repository, ScmFileSet fileSet, boolean recursive, ScmVersion version )
        throws ScmException;
}
