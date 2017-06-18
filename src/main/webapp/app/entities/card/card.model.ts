import { Cenario } from '../cenario';
export class Card {
    constructor(
        public id?: number,
        public nome?: string,
        public url?: string,
        public https?: boolean,
        public meta?: string,
        public previa?: string,
        public disposicao?: string,
        public tipo?: number,
        public linha?: number,
        public coluna?: number,
        public modo?: string,
        public caminho?: string,
        public arquivo?: string,
        public extensao?: string,
        public largura?: number,
        public classe?: string,
        public codigo?: string,
        public cenario?: Cenario,
    ) {
        this.https = false;
    }
}
