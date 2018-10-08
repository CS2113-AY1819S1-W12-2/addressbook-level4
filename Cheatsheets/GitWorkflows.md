# Types of workflows

## Forking flow
In the forking workflow, the 'official' version of the software is kept in a remote repo designated as the 'main repo'. All team members fork the main repo create pull requests from their fork to the main repo.

## Centralized revision control
Uses a central remote repo that is shared by the team. Team members download (‘pull’) and upload (‘push’) changes between their own local repositories and the central repository.
Similar to what we did for CG1112 when we all don't know how to use github to its fullest extent

## Distributed (Decentralised) RCS
Allows multiple remote repos and pulling and pushing can be done among them in arbitrary ways.

## Feature Branch Flow
Feature branch workflow is similar to forking workflow except there are no forks. Everyone is pushing/pulling from the same remote repo. The phrase feature branch is used because each new feature (or bug fix, or any other modification) is done in a separate branch and merged to *master* branch when ready.

