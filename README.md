# cpscommunicator

## Installation
1. Clone the repository
```shell
git clone https://github.com/KIT-TVA/cps-communicator.git
```

2. Install the submodules
```shell
git submodule update --init --recursive
```

## Build

Use gradle to build a JAR using the `shadowJar` command in [cps-communicator-base](cps-communicator-base)
 ```shell 
gradle shadowJar
```

## Run

Use the JAR to run the commands.
A list of available commands can be obtained by calling the JAR with `help`.

