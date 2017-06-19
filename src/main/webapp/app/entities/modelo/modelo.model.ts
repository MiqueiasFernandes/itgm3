import { User } from '../../shared';
export class Modelo {
    constructor(
        public id?: number,
        public nome?: string,
        public cor?: string,
        public formula?: string,
        public funcao?: string,
        public variaveis?: string,
        public palpite?: string,
        public parametros?: string,
        public requires?: string,
        public codigo?: string,
        public user?: User,
    ) {
    }
}
