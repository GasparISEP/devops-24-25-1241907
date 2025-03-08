# CA1: Version Control with Git

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

## Table of Contents

- [Introduction](#introduction)

- [Environment Setup](#environment-setup)

- [Week 1: Development Without Branches](#week-1-development-without-branches)
    - [Goals and Requirements](#goals-and-requirements)
    - [Key Developments](#key-developments)


## Introduction
This document outlines the assignment on Version Control using Git for the DevOps course. 
The task is divided into two primary segments: in Step 1, a straightforward approach to version control is employed without the use of branches, whereas in Step 2, branching is introduced to support the integration of new features and the correction of bugs. 
The Final Results section showcases the project’s outcomes, illustrating how the application has progressively evolved through the implementation of new functionalities and the resolution of issues. 
Additionally, the report examines an Alternative Approach by assessing Subversion (SVN), comparing its attributes and potential applicability in achieving the assignment’s objectives.


## Environment Setup
Initially, I cloned a repository that already contained the Tutorial React.js and Spring Data REST application, thereby creating a local duplicate of the tutorial project. 
Afterward, I established a personal repository specifically for hosting the class assignments, ensuring that every development change was systematically tracked via version control.

**Creating My Repository:**
I set up a dedicated folder on my local machine for the DevOps class assignments and converted it into a Git repository.
This action marked the initial phase in creating my project workspace.

```shell
mkdir ~/devops-24-25-1241907
cd ~/devops-24-25-1241907
git init
```
**Copying the Tutorial Application:** 
In order to incorporate the tutorial application into my project, 
I transferred all of its files into my repository. 
This procedure guaranteed that every critical component needed for the assignment was systematically maintained under version control.

```shell
cp -r ~/basic ~/devops-24-25-1241907
```

**Linking to GitHub:**
After transferring the tutorial application into my repository, I proceeded to associate my local repository with a newly created GitHub repository.
This setup enabled me to push my updates to a remote server, ensuring both reliable backup and effortless sharing.

```shell
git remote add origin git@github.com:GasparISEP/devops-24-25-1241907.git
```
**First Commit:**
Once the repository was fully configured and all necessary files were verified to be in place, I proceeded to add the README file. 
This initial addition was committed with the message “initial commit”, marking the official start of my assignment work.

```shell
echo "# devops-24-25-1241907" >> README.md
git add . README.md
git commit -m "initial commit"
```
**Pushing to Remote:**
Finally, I pushed my inaugural commit to the GitHub repository, thereby initiating the remote version history for my assignments.

```shell
git branch -M main
git push -u origin main
```
This approach provided a tidy and well-structured beginning for my class assignments, establishing a direct connection to the original tutorial application while keeping my repository ready for all future developments.


## Step 1: Development Without Branches

### Goals and Requirements
- The first segment of the assignment is dedicated to mastering basic version control techniques without using branching.
- The tasks involve setting up the project environment, making direct modifications to the primary branch, and committing these changes.
- A key objective is to integrate a new functionality (for example, adding a jobYears attribute to an Employee object) while ensuring proper version tagging—beginning with an initial version and updating it after the new feature is implemented.
- The overall focus is on practicing commit procedures, understanding the evolution of the commit history, and effectively using tags for versioning.

### Key Developments

1. **Opened an issue on the remote repository via GitHub.**

The title of the issue is “In this first part of the exercise, we will use only the master branch. #1”.
This was done to document and communicate that, for this initial phase of the exercise, all development should occur exclusively on the main branch, ensuring a straightforward and unified version control workflow.

2. **Transfer the code of the Tutorial React.js and Spring Data REST Application into a new folder named part1, which is located inside the CA1 directory.**

```shell
mkdir CA1/part1
cp -r ~/devops-24-25-1241907/basic ~/devops-24-25-1241907/CA1/part1
```

3. **Commit the changes and then push them.**

Once the application code was in the part1 directory and everything was running, I committed the changes to the main branch, referencing the issue I had opened earlier.

```shell
git add .
git commit -m "Initialize repository and connect to remote (#1)"
git push
```

4. **Opened a sub-issue linked to the main issue.**

The sub-issue was titled “Tag the initial version. #2”, following a proper procedure by using the issue system for the entire development of the assignment.

5. **Tag the repository to mark the application’s version.**

Following the versioning pattern outlined in the assignment (major.minor.revision), I tagged the initial setup as v1.1.0 and subsequently pushed that tag to the remote repository.

```shell
git tag -a v1.1.0 -m "v1.1.0"
git push origin v1.1.0
```
Sub-issue closed.

