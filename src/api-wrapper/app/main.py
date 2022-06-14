import aiofiles
import os
import subprocess
from dotenv import load_dotenv
from fastapi import FastAPI, File, UploadFile
from tempfile import TemporaryDirectory

from .demands_analysis import RawDemandsAnalysis, AggregatedDemandsAnalysis

# load .env file
load_dotenv()

# create application
app = FastAPI()

# load the spacenet jar path from environment variable
spacenet_jar = os.getenv("SPACENET_PATH")


@app.post("/demands-raw", response_model=RawDemandsAnalysis)
async def analyze_raw_demands(file: UploadFile = File(...)):
    # create a temp directory for working files
    with TemporaryDirectory() as tempdir:
        scenario_path = os.path.join(tempdir, file.filename)
        results_path = os.path.join(tempdir, "results.json")
        # write scenario file to temporary directory
        async with aiofiles.open(scenario_path, "wb") as scenario_file:
            content = await file.read()
            await scenario_file.write(content)
        # call spacenet headless script
        subprocess.call(
            [
                "java",
                "-jar",
                spacenet_jar,
                "-h",
                "demands-raw",
                "-i",
                scenario_path,
                "-o",
                results_path,
            ]
        )
        # read analysis outputs
        async with aiofiles.open(results_path, "r") as results_file:
            results = await results_file.read()
    return RawDemandsAnalysis.parse_raw(results)


@app.post("/demands-agg", response_model=AggregatedDemandsAnalysis)
async def analyze_aggregated_demands(file: UploadFile = File(...)):
    # create a temp directory for working files
    with TemporaryDirectory() as tempdir:
        scenario_path = os.path.join(tempdir, file.filename)
        results_path = os.path.join(tempdir, "results.json")
        # write scenario file to temporary directory
        async with aiofiles.open(scenario_path, "wb") as scenario_file:
            content = await file.read()
            await scenario_file.write(content)
        # call spacenet headless script
        subprocess.call(
            [
                "java",
                "-jar",
                spacenet_jar,
                "-h",
                "demands-agg",
                "-i",
                scenario_path,
                "-o",
                results_path,
            ]
        )
        # read analysis outputs
        async with aiofiles.open(results_path, "r") as results_file:
            results = await results_file.read()
    return AggregatedDemandsAnalysis.parse_raw(results)
