#!/usr/bin/env
# -*- coding: utf-8 -*-
"""Setup the package."""

import subprocess
import setuptools

with open("README.md", mode="r", encoding="utf-8") as readme:
    long_description = readme.read()

with open("requirements.txt", mode="r", encoding="utf-8") as reqs:
    requirements = reqs.read().splitlines()

setuptools.setup(
    name="cabCLI",
    version="0.1",
    author="James O Hortle",
    author_email="jamesohortle@gmail.com",
    description="CLI to access NYC cab data.",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/jamesohortle/NYCCab",
    packages=setuptools.find_packages(),
    classifiers=[
        "Programming Language :: Python :: 3",
        "Operating System :: Mac, Linux",
    ],
    python_requires="==3.6",
    install_requires=requirements,
    package_data={},
    include_package_data=True,
)
