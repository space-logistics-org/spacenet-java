# API Wrapper for SpaceNet Java

## Installation

1. Install the minimal Python dependency requirements with the shell command:
```
pip install -r requirements.txt
```

2. Create a `.env` file in this directory, and specify the location of your
SpaceNet Java Archive (JAR) file. For example:
```
SPACENET_PATH = ../../target/spacenet-2.5.1464-jar-with-dependencies.jar
```

## Usage

Start the application with the shell command:
```
uvicorn app.main:app --reload
```
the API service is available at <http://localhost:8000/docs>.

Upload scenario files (.json or .xml) to view demands analysis results.
