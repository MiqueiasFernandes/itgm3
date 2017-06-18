import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ItgmProjetoModule } from './projeto/projeto.module';
import { ItgmCenarioModule } from './cenario/cenario.module';
import { ItgmBaseModule } from './base/base.module';
import { ItgmCustomizeModule } from './customize/customize.module';
import { ItgmModeloModule } from './modelo/modelo.module';
import { ItgmModeloExclusivoModule } from './modelo-exclusivo/modelo-exclusivo.module';
import { ItgmCardModule } from './card/card.module';
import { ItgmCompartilharModule } from './compartilhar/compartilhar.module';
import { ItgmTerminalModule } from './terminal/terminal.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ItgmProjetoModule,
        ItgmCenarioModule,
        ItgmBaseModule,
        ItgmCustomizeModule,
        ItgmModeloModule,
        ItgmModeloExclusivoModule,
        ItgmCardModule,
        ItgmCompartilharModule,
        ItgmTerminalModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmEntityModule {}
