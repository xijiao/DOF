#!/bin/bash

git filter-branch --env-filter \
	'if [ git $GIT_COMMIT = "4bd0433c25d10554f2eada0aaefee80846ca2068" ] then
		export GIT_AUTHOR_DATE="Mon Mar 7 21:32:46 2016 +0800";
		export GIT_COMMITTER_DATE="Mon Mar 7 21:35:41 2016 +0800";
	fi'
