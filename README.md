# Hotel Search
_Example Project_

This is a sample project that shows a list of hotels that can be sorted or shown on Google Maps.

I created it to demonstrate modern Android in a small codebase. Hopefully you'll find it useful!

I'm proud of how quickly this came together.
The whole project took 16 hours&mdash;major functionality took 6 hours of focused effort (including 1 hour to learn
about Google Maps) with the remaining time spent casually refactoring.

**IMPORTANT: You must insert your own Google API Key by replacing YOUR_API_KEY in AndroidManifest.xml.**

## Features

- Splash screen
- Launcher icon
- Dagger
- Jetpack: viewmodel, livedata w/switchmap, room, paging library
- Retrofit w/moshi
- Picasso
- Fragments (w/postponeEnterTransition)
- Kotlin w/ktx
- Coroutines
- BigDecimal
- Data bindings, bindingadapters, converters
- String resources with placeholders

## Todo

- Break out fragment specific viewmodels
- Lots of silly kotlin things like if/let
- Use ktx inn uiutils
- Use viewmodel state library
- Make list/map children of a wrapper fragment
- Adapter.submitlist(null) vs just scroll to 0
- Move getSortSubTitle into hotelsort
- Take camera lat/lng out of layout xml
- Extensions instead of utils

---

Copyright 2020 codeparams.com. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.