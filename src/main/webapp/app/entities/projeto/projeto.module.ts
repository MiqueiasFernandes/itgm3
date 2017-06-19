import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItgmSharedModule } from '../../shared';
import { ItgmAdminModule } from '../../admin/admin.module';
import {
    ProjetoService,
    ProjetoPopupService,
    ProjetoComponent,
    ProjetoDetailComponent,
    ProjetoDialogComponent,
    ProjetoPopupComponent,
    ProjetoDeletePopupComponent,
    ProjetoDeleteDialogComponent,
    projetoRoute,
    projetoPopupRoute,
    ProjetoResolvePagingParams,
} from './';
import { FabAddProjetoComponent } from './fab-add-projeto/fab-add-projeto.component';
import { SelecionarProjetoComponent } from './selecionar-projeto/selecionar-projeto.component';

const ENTITY_STATES = [
    ...projetoRoute,
    ...projetoPopupRoute,
];

@NgModule({
    imports: [
        ItgmSharedModule,
        ItgmAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProjetoComponent,
        ProjetoDetailComponent,
        ProjetoDialogComponent,
        ProjetoDeleteDialogComponent,
        ProjetoPopupComponent,
        ProjetoDeletePopupComponent,
        FabAddProjetoComponent,
        SelecionarProjetoComponent,
    ],
    entryComponents: [
        ProjetoComponent,
        ProjetoDialogComponent,
        ProjetoPopupComponent,
        ProjetoDeleteDialogComponent,
        ProjetoDeletePopupComponent,
        FabAddProjetoComponent,
        SelecionarProjetoComponent
    ],
    providers: [
        ProjetoService,
        ProjetoPopupService,
        ProjetoResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmProjetoModule {}
