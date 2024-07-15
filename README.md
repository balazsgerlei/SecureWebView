# SecureWebView
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)
[![license](https://img.shields.io/github/license/balazsgerlei/SecureWebView)](https://creativecommons.org/publicdomain/zero/1.0/)
[![last commit](https://img.shields.io/github/last-commit/balazsgerlei/SecureWebView?color=018786)](https://github.com/balazsgerlei/SecureWebView/commits/main)
[![](https://jitpack.io/v/balazsgerlei/SecureWebView.svg)](https://jitpack.io/#balazsgerlei/SecureWebView)

Android WebView wrapper with secure defaults to avoid security issues caused by misconfiguring WebViews.

Still under heavy development, breaking API changes are expected!


## Setup

The library is currently hosted on `jitpack.io`. You can add it as a depenency to your project:

Step 1. Add the JitPack repository to your build file

Add it in your root `build.gradle` at the end of repositories:

	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.balazsgerlei:SecureWebView:1.0.0-alpha03'
	}


