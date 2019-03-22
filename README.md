# sylk4j
A library to read and write SYmbolic LinK (SYLK, .slk) files in java. See https://en.wikipedia.org/wiki/SYmbolic_LinK_(SYLK) for details on the format.

There are two representations: `SylkRecord`s represent the low level records as found in the files. The `SylkSheet` represents a whole sheet, containing multiple `SylkCells`. 

## Status
Only basic value reading is implemented at the moment.

## Sample

```
SylkParser parser = new SylkParser();
SylkSheet sheet = parser.parseSheet(reader);
sheet.getCell(0,0).getString();
```