import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItgmSharedModule } from '../../shared';
import {
    ModeloExclusivoService,
    ModeloExclusivoPopupService,
    ModeloExclusivoComponent,
    ModeloExclusivoDetailComponent,
    ModeloExclusivoDialogComponent,
    ModeloExclusivoPopupComponent,
    ModeloExclusivoDeletePopupComponent,
    ModeloExclusivoDeleteDialogComponent,
    modeloExclusivoRoute,
    modeloExclusivoPopupRoute,
    ModeloExclusivoResolvePagingParams,
} from './';
import { MapearModeloComponent } from './mapear-modelo/mapear-modelo.component';

const ENTITY_STATES = [
    ...modeloExclusivoRoute,
    ...modeloExclusivoPopupRoute,
];

@NgModule({
    imports: [
        ItgmSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ModeloExclusivoComponent,
        ModeloExclusivoDetailComponent,
        ModeloExclusivoDialogComponent,
        ModeloExclusivoDeleteDialogComponent,
        ModeloExclusivoPopupComponent,
        ModeloExclusivoDeletePopupComponent,
        MapearModeloComponent,
    ],
    entryComponents: [
        ModeloExclusivoComponent,
        ModeloExclusivoDialogComponent,
        ModeloExclusivoPopupComponent,
        ModeloExclusivoDeleteDialogComponent,
        ModeloExclusivoDeletePopupComponent,
    ],
    providers: [
        ModeloExclusivoService,
        ModeloExclusivoPopupService,
        ModeloExclusivoResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmModeloExclusivoModule {}
