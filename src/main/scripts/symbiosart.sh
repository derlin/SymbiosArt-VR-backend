#!/bin/bash

## inspired by the https://jenkins.daplab.ch/job/Trumpet/ artifact
# Make sure umask is sane
umask 022

# Automatically mark variables and functions which are modified or created
# for export to the environment of subsequent commands.
set -o allexport

# Set up a default search path.
PATH="/sbin:/usr/sbin:/bin:/usr/bin"
export PATH

scriptdir=`dirname $0`/..
HOMEDIR=${HOMEDIRPRIME:-`readlink -f $scriptdir`}

appdir=$HOMEDIR/..
APPDIR=${APPDIRPRIME:-`readlink -f $appdir`}

SRCDIR=${SRCDIRPRIME:-$HOMEDIR/src}
LIBDIR=${LIBDIRPRIME:-$HOMEDIR/lib}
CONFDIR=${SRCONFDIRPRIME:-$HOMEDIR/config}

# Update path so scripts under bin are available
PATH=$HOMEDIR/bin:$PATH

cd $HOMEDIR

## Generate classpath from libraries in $LIBDIR, and add $CONFDIR if required
OUR_CLASSPATH=$(find $LIBDIR -type f -name "*.jar" | paste -sd:)
if [ -d "$CONFDIR" ]; then
    OUR_CLASSPATH=$CONFDIR:$OUR_CLASSPATH
fi

JAVA_OPTS="$JAVA_OPTS -Dlogback.configurationFile=logback.xml"
#JAVA_OPTS="$JAVA_OPTS -Dlog4j.configuration=log4j-production.properties"

command="hadoop jar $LIBDIR/SymbiosArt-JettyServer.jar"

echo $command $@
$command $@
