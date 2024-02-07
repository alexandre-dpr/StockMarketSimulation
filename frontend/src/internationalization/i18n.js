import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
// Importer les fichiers JSON directement
import enTranslations from './en.json';
import frTranslations from './fr.json';

i18n
    .use(initReactI18next) // passe l'instance i18n à react-i18next
    .use(LanguageDetector) // détecte la langue du navigateur
    .init({
        // les ressources à charger
        resources: {
            en: {
                translation: enTranslations
            },
            fr: {
                translation: frTranslations
            }
        },
        fallbackLng: 'en', // langue à utiliser si celle du navigateur n'est pas disponible
        interpolation: {
            escapeValue: false // réagit déjà à l'échappement des valeurs
        }
    });

export default i18n;
