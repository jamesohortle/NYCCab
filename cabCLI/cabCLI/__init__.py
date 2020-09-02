#!/usr/bin/env
# -*- coding: utf-8 -*-

import re
import argparse
from datetime import date

import requests
from tabulate import tabulate

## Parse arguments.
parser = argparse.ArgumentParser(
    description="Get number of trips for medallions m on date d."
)
parser.add_argument(
    "--medallions",
    "-m",
    metavar="m",
    type=str,
    nargs="+",
    action="extend",
    help="Medallion to check as strings. Each medallion is a 32-digit hexadecimal value without the 0x prefix. Multiple medallions accepted with each -m flag.",
    required=True,
)
parser.add_argument(
    "--date",
    "-d",
    metavar="d",
    type=str,
    help="Date to filter by. Only one date accepted. Only acceptable format for now is yyyy-mm-dd (ISO format). Defaults to today's date in your device's timezone.",
    default=date.today().isoformat(),
    required=False,
)

args = parser.parse_args()
## Validate medallion as 32-digit hex number.
validMedallionPattern = re.compile(r"[0-9A-F]{32}", flags=re.IGNORECASE)


def validateMedallions(medallion: str) -> bool:
    if len(medallion) != 32:
        return False
    if not validMedallionPattern.match(medallion):
        return False
    return True


# Validate and format medallion strings.
validMedallions = list(map(str.upper, filter(validateMedallions, args.medallions)))

if not validMedallions:
    quit("No valid medallions.")


## Validate in yyyy-mm-dd pattern.
validDatePattern = re.compile(r"\d{4}-[0-1]\d-[0-3]\d")


def validateDate(date: str) -> bool:
    if not validDatePattern.match(date):
        return False
    return True


if not validateDate(args.date):
    quit("Date not valid.")

## Make request.
endpoint = "http://localhost:8080/tripCount"
payload = {"date": args.date, "medallion": args.medallions}

r = requests.get(endpoint, params=payload)


## Format and exit.
def formatPrintResponse(r: requests.Response) -> None:
    print(tabulate(r.json(), headers="keys"))


formatPrintResponse(r)