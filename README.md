# media-offload

A Kotlin CLI tool that ingests raw media files from cameras and phones, reads their EXIF metadata, renames them to a consistent format, organises them into date-based folders, and deletes sidecar/metadata files.

---

## What it does

When you offload an SD card or phone storage you typically end up with a pile of files named `GH010123.MP4`, `DSC04512.ARW`, `IMG_20240517_143022.jpg`, and accompanying metadata junk (`*.thm`, `*.lrv`, `*.xml`). This tool:

1. **Discovers** all media files under a given directory (recursive, skips hidden files and the output folder)
2. **Reads EXIF** data from each file using `exiftool` — extracts camera model and original creation date
3. **Renames** each file to a uniform pattern and moves it into a dated subfolder under `SORTED_DST/`
4. **Cleans up** sidecar files (`.lrv`, `.thm`, `.xml`) and any empty directories left behind

---

## How it works

```
offload --path <dir>
        │
        ▼
  MediaFinderTask     — walks the directory, reads EXIF, prints a summary report
        │
        ▼
  MoveTask            — renames IMAGE + VIDEO files and moves them into SORTED_DST/<date>/
        │
        ▼
  CleanupTask         — deletes sidecar (OTHER) files and removes empty directories
```

### Directory structure convention

The tool infers the **device model** from the name of the top-level subfolder directly under the given path. Organise your offloaded files like this before running:

```
/Volumes/IMPORT/
├── A67/          ← device model label (used as-is in the filename)
│   └── DCIM/
│       └── *.ARW
├── G12/
│   └── *.MP4
└── P10/
    └── *.jpg
```

Running `offload --path /Volumes/IMPORT` will label each file with the folder it came from (`A67`, `G12`, `P10`, etc.).

### Output filename format

```
{PREFIX}_{DATE}_{TIME}_{DEVICE}.{ext}
```

| Part   | Example        | Source                              |
|--------|----------------|-------------------------------------|
| PREFIX | `IMG` / `VID`  | Derived from file extension         |
| DATE   | `2024-05-17`   | EXIF "Date/Time Original"           |
| TIME   | `14-30-45`     | EXIF time, colons replaced by `-`   |
| DEVICE | `A67`          | Top-level subfolder name (uppercased)|

**Example:** `IMG_2024-05-17_14-30-45_A67.arw`

### Output folder structure

```
<path>/
└── SORTED_DST/
    ├── 2024-05-17/
    │   ├── IMG/
    │   │   └── IMG_2024-05-17_14-30-45_A67.arw
    │   └── VID/
    │       └── VID_2024-05-17_15-10-00_G12.mp4
    └── 2024-05-18/
        └── IMG/
            └── IMG_2024-05-18_09-05-33_P10.jpg
```

### Supported file types

| Category | Extensions                              | Behaviour         |
|----------|-----------------------------------------|-------------------|
| IMAGE    | `arw` `heic` `jpg` `jpeg` `png` `gif`  | Renamed and moved |
| VIDEO    | `mp4`                                   | Renamed and moved |
| OTHER    | `lrv` `thm` `xml`                       | Deleted (sidecars)|
| UNKNOWN  | anything else                           | Skipped           |

### EXIF reading

EXIF data is read by shelling out to `exiftool`. Dates are tried in this order:

1. `Date/Time Original`
2. `Create Date`
3. `Modify Date`

Camera model is tried in this order:

1. `Camera Model Name`
2. `Model`
3. `Android Model`

If no date is found, the file is still processed but a warning is printed. Files with no EXIF model fall back to the subfolder name.

### Console report

Before moving any files the tool prints three tables:

- **Media type breakdown** — counts of IMAGE / VIDEO / OTHER / UNKNOWN (plus a list of any unrecognised extensions)
- **Device breakdown** — file count per device folder
- **Date range** — file count per date

---

## Prerequisites

- Java 21+
- [`exiftool`](https://exiftool.org) installed and on your `PATH`

```bash
# macOS
brew install exiftool
```

---

## Usage

### Build

```bash
./gradlew bootJar
```

Produces `build/libs/media-offload-0.0.1-SNAPSHOT.jar`.

### Run

```bash
java -jar build/libs/media-offload-0.0.1-SNAPSHOT.jar offload --path /Volumes/IMPORT
```

Short flag:

```bash
java -jar build/libs/media-offload-0.0.1-SNAPSHOT.jar offload -p /Volumes/IMPORT
```

### Run via Gradle (development)

```bash
./gradlew bootRun --args='offload --path /Volumes/IMPORT'
```
