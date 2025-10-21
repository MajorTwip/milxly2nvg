# MILXLY to NVG 2.0.2 Converter

This tool should convert the poprietary filetype MILXLY used by KADAS or map.army into the internationally used NATO Vector Graphics NVG.
The goal is not a fully featured NVG-Creator, only the most necessary Types will be implemented to be able to reuse existing Layers.
Additionally there is no conversion 2025B to APP6D or similar planned.  

To build you will need the Schema definitions for NVG 2.0.2 and put it into /schemas/2012/10/nvg

### Tip:
If you want to provide the files over http (as it may be required by some applications to consume) you have multiple possibilities

```
python -m http.server
```

```bash
npm install serve -g
serve -p 8000
```

```
ruby -run -ehttpd . -p8000

```