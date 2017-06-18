import { Cenario } from '../cenario';
export class Terminal {
    constructor(
        public id?: number,
        public nome?: string,
        public url?: string,
        public status?: number,
        public cenario?: Cenario,
    ) {
    }
}
