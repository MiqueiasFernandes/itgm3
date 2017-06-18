import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItgmSharedModule } from '../../shared';
import { ItgmAdminModule } from '../../admin/admin.module';
import {
    ModeloService,
    ModeloPopupService,
    ModeloComponent,
    ModeloDetailComponent,
    ModeloDialogComponent,
    ModeloPopupComponent,
    ModeloDeletePopupComponent,
    ModeloDeleteDialogComponent,
    modeloRoute,
    modeloPopupRoute,
    ModeloResolvePagingParams,
} from './';
import { FabAddModeloComponent } from './fab-add-modelo/fab-add-modelo.component';

const ENTITY_STATES = [
    ...modeloRoute,
    ...modeloPopupRoute,
];

@NgModule({
    imports: [
        ItgmSharedModule,
        ItgmAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ModeloComponent,
        ModeloDetailComponent,
        ModeloDialogComponent,
        ModeloDeleteDialogComponent,
        ModeloPopupComponent,
        ModeloDeletePopupComponent,
        FabAddModeloComponent,
    ],
    entryComponents: [
        ModeloComponent,
        ModeloDialogComponent,
        ModeloPopupComponent,
        ModeloDeleteDialogComponent,
        ModeloDeletePopupComponent,
    ],
    providers: [
        ModeloService,
        ModeloPopupService,
        ModeloResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmModeloModule {}
