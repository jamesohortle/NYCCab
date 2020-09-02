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
    "--medallion",
    "-m",
    metavar="m",
    type=str,
    nargs="+",
    action="extend",
    help="Medallion(s) to check as string(s). Each medallion is a 32-digit hexadecimal value without the 0x prefix. Multiple medallions accepted with each -m flag.",
    required=False,
)
parser.add_argument(
    "--date",
    "-d",
    metavar="d",
    type=str,
    help="Date to filter by. Only one date accepted. Only acceptable format for now is yyyy-mm-dd (ISO format). If unspecified, defaults to today's date in your device's timezone.",
    default=date.today().isoformat(),
    required=False,
)
parser.add_argument(
    "--no_cache",
    "-nc",
    action="store_true",
    help="Tell the server to return uncached, fresh results. If flag not present, results are cached by default. No arguments accepted.",
    required=False,
)
parser.add_argument(
    "--clear_cache",
    "-cc",
    action="store_true",
    help="Tell the server to clear its cache. If specified, request to clear cache will be sent before other results are calculated and received (two total requests). No arguments accepted.",
    required=False,
)

args = parser.parse_args()

## Clear cache.
if args.clear_cache:
    endpoint = "http://localhost:8080/clearCache"
    r = requests.get(endpoint)
    if not r.status_code == 200:
        print(
            "\033[93m"  # Set terminal color.
            "#############################################\n"
            f"# Warning! Cache clear failed (status {r.status_code}). #\n"
            "# The following results may be from cache.  #\n"
            "#############################################\n"
            "\033[0m"  # Revert terminal color.
        )
    else:
        print("\033[92mCache clear request succeeded!\033[0m")
        if args.medallions is None:
            quit(
                "\033[93mCache clear request succeeded but no medallions specified.\033[0m"
            )


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
    quit("\033[91mNo valid medallions.\033[0m")


## Validate in yyyy-mm-dd pattern.
validDatePattern = re.compile(r"\d{4}-[0-1]\d-[0-3]\d")


def validateDate(date: str) -> bool:
    if not validDatePattern.match(date):
        return False
    return True


if not validateDate(args.date):
    quit("\033[91mDate not valid.\033[0m")

## Check and format cache parameter.
cacheParam = "false" if args.no_cache else "true"

## Make request.
endpoint = "http://localhost:8080/tripCount"
payload = {"date": args.date, "medallion": args.medallions, "cache": cacheParam}

r = requests.get(endpoint, params=payload)

## Format and exit.
def formatPrintResponse(r: requests.Response) -> None:
    print(tabulate(r.json(), headers="keys"))


formatPrintResponse(r)
