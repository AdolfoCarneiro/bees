# 07 — License and Source Policy

## 1. Purpose

This document records decisions needed before public release:

```txt
- source code license
- asset license
- contribution policy
- third-party dependency policy
- source availability
```

## 2. Source Availability

Recommended:

```txt
Public GitHub repository
```

Reason:

```txt
- easier issue tracking
- easier modpack author trust
- easier AI-assisted review
- clearer release history
```

## 3. Code License Decision

Decision required before public release.

Common options:

```txt
MIT
LGPL-3.0
MPL-2.0
ARR / custom license
```

### MIT

Pros:

```txt
simple
permissive
easy for contributors/modpack ecosystem
```

Cons:

```txt
allows broad reuse with few restrictions
```

### LGPL-3.0

Pros:

```txt
keeps library-style modifications more open
```

Cons:

```txt
more complex
```

### MPL-2.0

Pros:

```txt
file-level copyleft
balanced for open projects
```

Cons:

```txt
less common for some Minecraft modders
```

### Custom / All Rights Reserved

Pros:

```txt
maximum control
```

Cons:

```txt
less friendly to contributors
less clear for reuse
```

## 4. Initial Recommendation

For a solo project that may accept contributions later:

```txt
MPL-2.0 or MIT
```

If you want maximum simplicity:

```txt
MIT
```

If you want modifications to core files to remain open:

```txt
MPL-2.0
```

Do not choose blindly. Decide before public release.

## 5. Asset License

Assets can use the same license as code or a separate license.

Recommended:

```txt
Code: MIT or MPL-2.0
Assets: CC BY-NC-SA or ARR if you want more control
```

But separate licensing can confuse contributors. Simpler option:

```txt
Use one project license for everything unless there is a strong reason not to.
```

## 6. Third-Party Asset Policy

Do not include:

```txt
- copied Forestry assets
- copied Productive Bees assets
- copied Minecraft textures
- AI-generated assets based on copyrighted characters/styles
- unlicensed Blockbench models from others
```

Use:

```txt
- original assets
- placeholders made for the project
- properly licensed resources with attribution
```

## 7. Contribution Policy

Before accepting PRs:

```txt
- add CONTRIBUTING.md
- define code style
- define test expectations
- define whether AI-generated code is allowed
- require contributors to agree their contribution can be licensed under project license
```

## 8. AI-Generated Assets/Code

If AI is used:

```txt
- review everything manually
- do not include assets that imitate specific copyrighted works
- keep prompts and source files when useful
- verify code compiles and tests pass
```

## 9. Required Release Files

Before public release:

```txt
LICENSE
README.md
CHANGELOG.md
docs/
```

Optional:

```txt
CONTRIBUTING.md
CODE_OF_CONDUCT.md
SECURITY.md
```
