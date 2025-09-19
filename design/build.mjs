import StyleDictionary from 'style-dictionary';

const sd = await StyleDictionary.loadConfig('config.json');
await sd.buildAllPlatforms();
